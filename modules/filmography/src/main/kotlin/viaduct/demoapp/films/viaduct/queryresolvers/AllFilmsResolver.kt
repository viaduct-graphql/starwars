package viaduct.demoapp.films.viaduct.queryresolvers

import viaduct.api.Resolver
import viaduct.api.grts.Film
import viaduct.demoapp.filmography.resolverbases.QueryResolvers
import viaduct.demoapp.films.models.repository.FilmsRepository
import viaduct.demoapp.films.viaduct.mappers.FilmBuilder

private const val DEFAULT_PAGE_SIZE = 10

/**
 * Resolver for the `allFilms` query in the Star Wars GraphQL API.
 *
 * This resolver fetches a list of films, limited by the provided argument or a default page size.
 */
@Resolver
class AllFilmsResolver : QueryResolvers.AllFilms() {
    override suspend fun resolve(ctx: Context): List<Film?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val films = FilmsRepository.getAllFilms().take(limit)

        // Convert StarWarsData.Film objects to Film objects
        return films.map { FilmBuilder(ctx).build(it) }
    }
}
