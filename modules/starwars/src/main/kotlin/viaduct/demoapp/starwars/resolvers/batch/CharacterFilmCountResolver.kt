package viaduct.demoapp.starwars.resolvers.batch

import viaduct.api.FieldValue
import viaduct.api.Resolver
import viaduct.demoapp.starwars.data.StarWarsData
import viaduct.demoapp.starwars.resolverbases.CharacterResolvers

/**
 * Demonstrates **Simple Batch Aggregation** for counting operations.
 *
 * ## When to Use
 * - Counting relationships (films per character, comments per post)
 * - Simple calculations (totals, averages)
 * - Operations that benefit from batching but don't require complex object building
 *
 * ## How batchResolve Works
 *
 * The `batchResolve()` method is called by the framework when multiple field requests
 * are collected for the same resolver. Instead of calling `resolve()` individually
 * for each character, the framework groups them together.
 *
 * ### What `contexts` Contains
 * Each `Context` in the list represents one field resolution request:
 * ```kotlin
 * // For query: allCharacters { filmCount }
 * contexts = [
 *     Context(Character(id="1")),  // Luke's filmCount request
 *     Context(Character(id="2")),  // Leia's filmCount request
 *     Context(Character(id="3"))   // Han's filmCount request
 * ]
 * ```
 *
 * Each context provides:
 * - `ctx.objectValue`: The Character object this field belongs to
 * - `ctx.arguments`: Any arguments passed to the filmCount field
 * - Framework data for building results
 *
 * ## Efficiency
 * Instead of N individual count operations, performs one batch lookup and maps results.
 */
@Resolver(objectValueFragment = "fragment _ on Character { id }")
class CharacterFilmCountResolver : CharacterResolvers.FilmCount() {
    override suspend fun batchResolve(contexts: List<Context>): List<FieldValue<Int>> {
        val characterIds = contexts.map { it.objectValue.getId().internalID }

        val filmCounts = characterIds.associateWith { characterId ->
            StarWarsData.characterFilmRelations[characterId]?.size ?: 0
        }

        return contexts.map { ctx ->
            val characterId = ctx.objectValue.getId().internalID
            FieldValue.ofValue(filmCounts[characterId] ?: 0)
        }
    }
}
