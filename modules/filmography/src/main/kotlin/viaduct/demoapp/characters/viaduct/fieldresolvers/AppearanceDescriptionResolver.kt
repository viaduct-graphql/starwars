package viaduct.demoapp.characters.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.filmography.resolverbases.CharacterResolvers

/**
 * Example of full fragment syntax for complex computed fields
 *
 * @resolver("fragment _ on Character { name eyeColor hairColor }"): Fragment definition to
 *          fetch multiple appearance-related fields to create a description
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
class AppearanceDescriptionResolver : CharacterResolvers.AppearanceDescription() {
    override suspend fun resolve(ctx: Context): String? {
        // Gets the character from the context with the data specified in the @Resolver
        val character = ctx.objectValue

        // Builds a description using the fetched fields, providing defaults if any are missing
        val name = character.getName() ?: "Someone"
        val eyeColor = character.getEyeColor() ?: "unknown eyes"
        val hairColor = character.getHairColor() ?: "unknown hair"

        return "$name has $eyeColor eyes and $hairColor hair"
    }
}
