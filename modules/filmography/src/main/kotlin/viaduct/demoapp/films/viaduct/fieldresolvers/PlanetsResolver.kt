package viaduct.demoapp.films.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.api.grts.Planet
import viaduct.demoapp.characters.models.repository.CharacterRepository
import viaduct.demoapp.filmography.resolverbases.FilmResolvers
import viaduct.demoapp.films.models.repository.FilmCharactersRepository

/**
 * Example of a relationship field resolver in the Film type.
 *
 * This resolver fetches the list of unique homeworld planets of main characters appearing in a film.
 *
 * @resolver("fragment _ on Film { id }"): Fragment syntax for accessing film ID
 */
@Resolver("id")
class PlanetsResolver : FilmResolvers.Planets() {
    override suspend fun resolve(ctx: Context): List<Planet?>? {
        val filmId = ctx.objectValue.getId().internalID

        val characterIds = FilmCharactersRepository.findCharactersByFilmId(filmId)

        val planetIds = characterIds.mapNotNull { CharacterRepository.findById(it)?.homeworldId }.toSet()

        return planetIds.map {
            val globalId = ctx.globalIDFor(Planet.Reflection, it)
            ctx.nodeFor(globalId)
        }
    }
}
