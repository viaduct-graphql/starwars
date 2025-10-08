package viaduct.demoapp.films.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.demoapp.characters.models.repository.CharacterRepository
import viaduct.demoapp.characters.viaduct.mappers.CharacterBuilder
import viaduct.demoapp.filmography.resolverbases.FilmResolvers
import viaduct.demoapp.films.models.repository.FilmCharactersRepository

/**
 * Example of a relationship field resolver in the Film type.
 *
 * This resolver fetches the list of main characters appearing in a film.
 *
 * @resolver("fragment _ on Film { id }"): Fragment syntax for accessing film ID
 */
@Resolver("id")
class CharactersResolver : FilmResolvers.Characters() {
    override suspend fun resolve(ctx: Context): List<Character?>? {
        val filmId = ctx.objectValue.getId().internalID

        return FilmCharactersRepository.findCharactersByFilmId(filmId).map {
            val character = CharacterRepository.findById(it) ?: throw IllegalArgumentException("Character with ID $it not found")
            CharacterBuilder(ctx).build(character)
        }
    }
}
