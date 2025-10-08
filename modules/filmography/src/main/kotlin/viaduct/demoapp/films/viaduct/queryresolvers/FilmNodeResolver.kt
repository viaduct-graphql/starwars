package viaduct.demoapp.films.viaduct.queryresolvers

import viaduct.api.Resolver
import viaduct.demoapp.filmography.NodeResolvers
import viaduct.demoapp.films.models.repository.FilmsRepository
import viaduct.demoapp.films.viaduct.mappers.FilmBuilder

/**
 * Node resolver for the Film type in the Star Wars GraphQL API.
 *
 * This resolver handles fetching a Film by its global ID.
 */
@Resolver
class FilmNodeResolver : NodeResolvers.Film() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Film {
        val filmId = ctx.id.internalID

        val film = FilmsRepository.findFilmById(filmId)
            ?: throw IllegalArgumentException("Film with ID $filmId not found")

        return FilmBuilder(ctx).build(film)
    }
}
