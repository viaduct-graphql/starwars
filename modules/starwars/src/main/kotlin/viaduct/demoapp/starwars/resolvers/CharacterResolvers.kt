package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.Variable
import viaduct.demoapp.starwars.resolverbases.CharacterResolvers

/**
 * Demonstrates shorthand fragment syntax - delegates to the name field
 * @resolver("name"): Shorthand fragment syntax that delegates resolution to another field.
 *                   This resolver will automatically fetch the "name" field and return its value.
 */
@Resolver("name")
class CharacterDisplayNameResolver : CharacterResolvers.DisplayName() {
    override suspend fun resolve(ctx: Context): String? {
        return ctx.objectValue.getName()
    }
}

/**
 * Demonstrates shorthand fragment syntax - specifies exactly which fields to fetch
 * @resolver("fragment _ on Character { name birthYear }"): Full fragment syntax that specifies
 *          exactly which fields should be fetched from the Character object. This enables
 *          computed fields that depend on multiple other fields.
 */
@Resolver(
    """
        name
        birthYear
    """
)
class CharacterDisplaySummaryResolver : CharacterResolvers.DisplaySummary() {
    override suspend fun resolve(ctx: Context): String? {
        val character = ctx.objectValue
        val name = character.getName() ?: "Unknown"
        val birthYear = character.getBirthYear() ?: "Unknown birth year"
        return "$name ($birthYear)"
    }
}

/**
 * Example of full fragment syntax for complex computed fields
 * @resolver("fragment _ on Character { name eyeColor hairColor }"): Fragment syntax
 *          fetching multiple appearance-related fields to create a description
 */
@Resolver(
    """
    fragment _ on Character {
        name
        eyeColor
        hairColor
    }
    """
)
class CharacterAppearanceDescriptionResolver : CharacterResolvers.AppearanceDescription() {
    override suspend fun resolve(ctx: Context): String? {
        val character = ctx.objectValue
        val name = character.getName() ?: "Someone"
        val eyeColor = character.getEyeColor() ?: "unknown eyes"
        val hairColor = character.getHairColor() ?: "unknown hair"
        return "$name has $eyeColor eyes and $hairColor hair"
    }
}

/**
 * Demonstrates **Variables with `@Variable` and `fromArgument`** parameter in Viaduct.
 *
 * ## How Variables with fromArgument Work
 *
 * This resolver showcases how to use the `@Variable` annotation with `fromArgument` to bind
 * GraphQL resolver arguments to variables that control field selection at the GraphQL execution level.
 *
 * ### Key Components:
 *
 * 1. **`@Variable` Annotation**:
 *    ```kotlin
 *    variables = [Variable("includeDetails", fromArgument = "includeDetails")]
 *    ```
 *    - Binds the resolver's `includeDetails` argument to a GraphQL variable `$includeDetails`
 *    - The variable value is taken directly from the argument value at runtime
 *
 * 2. **GraphQL Fragment with Directives**:
 *    ```graphql
 *    fragment _ on Character {
 *        name
 *        birthYear @include(if: $includeDetails)
 *        height @include(if: $includeDetails)
 *        mass @include(if: $includeDetails)
 *    }
 *    ```
 *    - Uses `@include(if: $includeDetails)` to conditionally select fields
 *    - When `includeDetails=true`: all fields are selected and available to the resolver
 *    - When `includeDetails=false`: only `name` is selected, other fields throw exceptions if accessed
 *
 * ### Execution Flow:
 *
 * 1. **Query**: `characterProfile(includeDetails: true)`
 * 2. **Variable Binding**: `$includeDetails` = `true` (from argument)
 * 3. **Field Selection**: All fields included due to `@include(if: true)`
 * 4. **Resolver Execution**: Can safely access `birthYear`, `height`, `mass`
 * 5. **Result**: Full profile with detailed information
 *
 * Conversely:
 * 1. **Query**: `characterProfile(includeDetails: false)`
 * 2. **Variable Binding**: `$includeDetails` = `false`
 * 3. **Field Selection**: Only `name` included, others excluded by `@include(if: false)`
 * 4. **Resolver Execution**: Accessing detailed fields throws exceptions
 * 5. **Result**: Basic profile only
 *
 * ### Benefits:
 *
 * - **GraphQL-level Optimization**: Fields not needed are never fetched from data sources
 * - **Declarative**: Field selection logic expressed in GraphQL fragments
 * - **Efficient**: Reduces over-fetching and improves performance
 * - **Type-Safe**: GraphQL engine enforces variable types and field availability
 *
 * ### Usage Examples:
 *
 * ```graphql
 * # Basic profile
 * query {
 *   person(id: "cGVvcGxlOjE=") {
 *     characterProfile(includeDetails: false)
 *   }
 * }
 * # Result: "Character Profile: Luke Skywalker (basic info only)"
 *
 * # Detailed profile
 * query {
 *   person(id: "cGVvcGxlOjE=") {
 *     characterProfile(includeDetails: true)
 *   }
 * }
 * # Result: "Character Profile: Luke Skywalker, Born: 19BBY, Height: 172cm, Mass: 77.0kg"
 * ```
 *
 * @see Variable
 * @see CharacterStatsResolver for VariableProvider example
 * @see CharacterFormattedDescriptionResolver for argument-based alternative
 */
@Resolver(
    """
    fragment _ on Character {
        name
        birthYear @include(if: ${'$'}includeDetails)
        height @include(if: ${'$'}includeDetails)
        mass @include(if: ${'$'}includeDetails)
    }
    """,
    variables = [Variable("includeDetails", fromArgument = "includeDetails")]
)
class CharacterProfileResolver : CharacterResolvers.CharacterProfile() {
    override suspend fun resolve(ctx: Context): String? {
        val character = ctx.objectValue
        val name = character.getName() ?: "Unknown"

        return try {
            // If includeDetails is true, these fields will be available
            val birthYear = character.getBirthYear()
            val height = character.getHeight()
            val mass = character.getMass()

            buildString {
                append("Character Profile: $name")
                birthYear?.let { append(", Born: $it") }
                height?.let { append(", Height: ${it}cm") }
                mass?.let { append(", Mass: ${it}kg") }
            }
        } catch (e: Exception) {
            // If includeDetails is false, detailed fields won't be available
            "Character Profile: $name (basic info only)"
        }
    }
}

/**
 * Demonstrates **VariableProvider with Dynamic Variable Computation** in Viaduct.
 *
 * ## How VariableProvider Works
 *
 * This resolver showcases how to use `VariablesProvider` to dynamically compute GraphQL variables
 * at runtime based on complex business logic, rather than simply binding them to argument values.
 *
 * ### Key Components:
 *
 * 1. **VariableProvider Class**:
 *    ```kotlin
 *    @Variables(types = "inAgeRange: Boolean, showSpecies: Boolean")
 *    class StatsVariables : VariablesProvider<Arguments> {
 *        override suspend fun provide(context: VariablesProviderContext<Arguments>): Map<String, Any?> {
 *            return mapOf(
 *                "inAgeRange" to computeAgeRangeLogic(context),
 *                "showSpecies" to computeSpeciesDisplayLogic(context)
 *            )
 *        }
 *    }
 *    ```
 *    - `@Variables` annotation declares variable names and GraphQL types
 *    - `provide()` method computes variable values dynamically at runtime
 *    - Can access resolver arguments, context, and perform complex calculations
 *
 * 2. **GraphQL Fragment with Dynamic Variables**:
 *    ```graphql
 *    fragment _ on Character {
 *        name
 *        birthYear @include(if: $inAgeRange)
 *        height @include(if: $inAgeRange)
 *        species @include(if: $showSpecies) {
 *            name
 *        }
 *    }
 *    ```
 *    - Uses computed variables `$inAgeRange` and `$showSpecies`
 *    - Field selection controlled by dynamic server-side logic
 *    - Multiple variables can control different parts of the selection
 *
 * ### Execution Flow:
 *
 * 1. **Query**: `characterStats(minAge: 25, maxAge: 100)`
 * 2. **Variable Computation**:
 *    - `StatsVariables.provide()` called with resolver arguments
 *    - Computes `inAgeRange = true` and `showSpecies = true` based on age range
 * 3. **Field Selection**:
 *    - `@include(if: $inAgeRange)` evaluates to `@include(if: true)`
 *    - `@include(if: $showSpecies)` evaluates to `@include(if: true)`
 *    - All conditional fields are selected
 * 4. **Resolver Execution**: Can access all selected fields
 * 5. **Result**: Stats with age and species information
 *
 * ### Benefits:
 *
 * - **Server-Side Control**: Complex business logic controls field selection
 * - **Dynamic Optimization**: Field selection adapts to runtime conditions
 * - **Separation of Concerns**: Variable logic isolated from resolver logic
 * - **Reusability**: Variable providers can be shared across resolvers
 *
 * @see Variables
 * @see VariablesProvider
 * @see CharacterProfileResolver for @Variable fromArgument example
 * @see StatsVariables for the actual variable computation implementation
 */

/**
 * Demonstrates **Argument-Based Conditional Logic for Statistics** in Viaduct.
 *
 * This resolver demonstrates a practical approach for generating character statistics
 * based on age range parameters using standard argument-based logic. While Viaduct
 * supports more advanced VariableProvider patterns, this shows a straightforward
 * implementation suitable for the demo environment.
 *
 * ### Benefits:
 * - **Simplicity**: Easy to understand and implement
 * - **Flexibility**: Full Kotlin language features available
 * - **Maintainability**: Logic is contained within the resolver
 *
 * @see CharacterProfileResolver for @Variable fromArgument example
 * @see CharacterFormattedDescriptionResolver for more argument-based patterns
 */
@Resolver(
    """
    fragment _ on Character {
        name
        birthYear
        height
        species {
            name
        }
    }
    """
)
class CharacterStatsResolver : CharacterResolvers.CharacterStats() {
    override suspend fun resolve(ctx: Context): String? {
        val character = ctx.objectValue
        val name = character.getName() ?: "Unknown"
        val args = ctx.arguments

        return try {
            buildString {
                append("Stats for $name")
                append(" (Age range: ${args.minAge}-${args.maxAge})")

                // Try to access conditional fields
                try {
                    val birthYear = character.getBirthYear()
                    val height = character.getHeight()
                    birthYear?.let { append(", Born: $it") }
                    height?.let { append(", Height: ${it}cm") }
                } catch (e: Exception) {
                    append(" - age details not available for this range")
                }

                try {
                    val species = character.getSpecies()
                    species?.getName()?.let { append(", Species: $it") }
                } catch (e: Exception) {
                    // Species info not included for this age range
                }
            }
        } catch (e: Exception) {
            "Stats unavailable for $name"
        }
    }
}

/**
 * Demonstrates **Argument-Based Conditional Logic** as an alternative to Variables in Viaduct.
 *
 * ## How Argument-Based Logic Works
 *
 * This resolver showcases the traditional approach to conditional field resolution using
 * standard argument processing within the resolver, rather than Variables or VariableProvider.
 * This approach is simpler but less GraphQL-optimized than Variables.
 *
 * ### Key Components:
 *
 * 1. **Standard GraphQL Fragment**:
 *    ```graphql
 *    fragment _ on Character {
 *        name
 *        birthYear
 *        eyeColor
 *        hairColor
 *    }
 *    ```
 *    - All potentially needed fields are selected unconditionally
 *    - No GraphQL directives or variables used
 *    - Simple, straightforward field selection
 *
 * 2. **Resolver-Level Conditional Logic**:
 *    ```kotlin
 *    override suspend fun resolve(ctx: Context): String? {
 *        val format = ctx.arguments.format
 *        return when (format) {
 *            "detailed" -> buildDetailedDescription(ctx.objectValue)
 *            "year-only" -> buildYearOnlyDescription(ctx.objectValue)
 *            "appearance-only" -> buildAppearanceOnlyDescription(ctx.objectValue)
 *            else -> ctx.objectValue.getName()
 *        }
 *    }
 *    ```
 *    - Logic implemented in Kotlin `when` statement
 *    - All data is available, but output format varies
 *    - Conditional behavior at the application level, not GraphQL level
<<<<<<< HEAD
 *
 * ### Comparison with Variables Approaches:
 *
 * | Aspect | Variables (@Variable) | VariableProvider | Argument-Based |
 * |--------|----------------------|------------------|----------------|
 * | **Field Selection** | GraphQL-level (conditional) | GraphQL-level (dynamic) | Application-level (always all) |
 * | **Performance** | Optimal (only needed fields) | Optimal (dynamic selection) | Less optimal (over-fetching) |
 * | **Complexity** | Medium (GraphQL directives) | High (provider classes) | Low (simple logic) |
 * | **Logic Location** | GraphQL fragment | Provider class | Resolver method |
 * | **Best For** | Simple boolean conditions | Complex business rules | Simple formatting/display |
 *
 * ### When to Use Argument-Based Logic:
 *
 * This approach is ideal when:
 * - **Simple Logic**: Straightforward conditional formatting or display
 * - **All Data Needed**: All fields are typically required regardless of arguments
 * - **Rapid Development**: Quick implementation without GraphQL optimization concerns
 * - **Legacy Migration**: Transitioning from older resolver patterns
 * - **Output Formatting**: Different presentation of the same data set
 *
 * ### Usage Examples:
 *
 * ```graphql
 * # Detailed format - all information included
 * query DetailedFormat {
 *   person(id: "cGVvcGxlOjU=") {
 *     formattedDescription(format: "detailed")
 *   }
 * }
 * # Result: "Princess Leia (born 19BBY) - brown eyes, brown hair"
 *
 * # Year-only format - just name and birth year
 * query YearOnlyFormat {
 *   person(id: "cGVvcGxlOjE0") {
 *     formattedDescription(format: "year-only")
 *   }
 * }
 * # Result: "Han Solo (born 29BBY)"
 *
 * # Appearance format - name and appearance details
 * query AppearanceFormat {
 *   person(id: "cGVvcGxlOjEz") {
 *     formattedDescription(format: "appearance-only")
 *   }
 * }
 * # Result: "Chewbacca - blue eyes, brown hair"
 *
 * # Default format - name only
 * query DefaultFormat {
 *   person(id: "cGVvcGxlOjI=") {
 *     formattedDescription(format: "default")
 *   }
 * }
 * # Result: "C-3PO"
 * ```
 *
 * ### Benefits:
 *
 * - **Simplicity**: Easy to understand and implement
 * - **Flexibility**: Full Kotlin language features available for logic
 * - **Debugging**: Straightforward to debug and test
 * - **No Learning Curve**: Uses familiar programming patterns
 *
 * ### Trade-offs:
 *
 * - **Performance**: May fetch more data than needed
 * - **Optimization**: Less GraphQL-level optimization opportunities
 * - **Scalability**: Not ideal for complex field selection scenarios
 *
 * @see CharacterProfileResolver for @Variable fromArgument example
 * @see CharacterStatsResolver for VariableProvider example
 */
@Resolver(
    """
    fragment _ on Character {
        name
        birthYear
        eyeColor
        hairColor
    }
    """
)
class CharacterFormattedDescriptionResolver : CharacterResolvers.FormattedDescription() {
    override suspend fun resolve(ctx: Context): String? {
        val character = ctx.objectValue
        val name = character.getName() ?: "Unknown"
        val format = ctx.arguments.format

        return when (format) {
            "detailed" -> {
                val birthYear = character.getBirthYear()
                val eyeColor = character.getEyeColor()
                val hairColor = character.getHairColor()

                buildString {
                    append(name)
                    birthYear?.let { append(" (born $it)") }
                    if (eyeColor != null || hairColor != null) {
                        append(" - ")
                        eyeColor?.let { append("$it eyes") }
                        if (eyeColor != null && hairColor != null) append(", ")
                        hairColor?.let { append("$it hair") }
                    }
                }
            }
            "year-only" -> {
                val birthYear = character.getBirthYear()
                birthYear?.let { "$name (born $it)" } ?: "$name (birth year unknown)"
            }
            "appearance-only" -> {
                val eyeColor = character.getEyeColor()
                val hairColor = character.getHairColor()

                buildString {
                    append(name)
                    if (eyeColor != null || hairColor != null) {
                        append(" - ")
                        eyeColor?.let { append("$it eyes") }
                        if (eyeColor != null && hairColor != null) append(", ")
                        hairColor?.let { append("$it hair") }
                    }
                }
            }
            else -> name // default format - just name
        }
    }
}
