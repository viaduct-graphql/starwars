package viaduct.demoapp.films.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.api.grts.Species
import viaduct.demoapp.characters.models.repository.CharacterRepository
import viaduct.demoapp.filmography.resolverbases.FilmResolvers
import viaduct.demoapp.films.models.repository.FilmCharactersRepository

/**
 * Example of a relationship field resolver in the Film type.
 *
 * This resolver fetches the list of unique species of main characters appearing in a film.
 *
 * @resolver("fragment _ on Film { id }"): Fragment syntax for accessing film ID
 */
@Resolver("id")
class SpeciesResolver : FilmResolvers.Species() {
    override suspend fun resolve(ctx: Context): List<Species?>? {
        val filmId = ctx.objectValue.getId().internalID

        val characterIds = FilmCharactersRepository.findCharactersByFilmId(filmId)

        val speciesIds = characterIds.mapNotNull { CharacterRepository.findById(it)?.speciesId }.toSet()

        return speciesIds.map {
            val globalId = ctx.globalIDFor(Species.Reflection, it)
            ctx.nodeFor(globalId)
        }
    }
}
