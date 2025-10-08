package viaduct.demoapp.characters.viaduct.queryresolvers

import viaduct.api.Resolver
import viaduct.demoapp.characters.models.repository.CharacterRepository
import viaduct.demoapp.characters.viaduct.mappers.CharacterBuilder
import viaduct.demoapp.filmography.resolverbases.QueryResolvers

/**
 * SearchCharacter Query resolver for the Star Wars GraphQL API.
 *
 * Allows searching for a character by name, ID, or birth year.
 * If multiple criteria are provided, the precedence is:
 * 1. byName: Returns the first matching character found for name searches.
 * 2. byId: Returns the character with the exact matching ID.
 * 3. byBirthYear: Returns the first matching character found for name or birth year searches.
 *
 * If no criteria are provided, returns null.
 *
 * Arguments:
 * - search: An object containing optional fields: byName (String), byId (ID), byBirthYear (String).
 */
@Resolver
class SearchCharacterResolver : QueryResolvers.SearchCharacter() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Character? {
        val search = ctx.arguments.search
        val byName = search.byName
        val byId = search.byId
        val byBirthYear = search.byBirthYear

        val character = when {
            byName != null -> CharacterRepository.findCharactersByName(byName).firstOrNull()
            byId != null -> CharacterRepository.findById(byId.internalID)
            byBirthYear != null -> CharacterRepository.findCharactersByYearOfBirth(byBirthYear).firstOrNull()
            else -> null
        }

        return character?.let {
            CharacterBuilder(ctx).build(character)
        }
    }
}
