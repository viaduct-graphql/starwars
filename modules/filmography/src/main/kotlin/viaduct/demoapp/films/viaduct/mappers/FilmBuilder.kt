package viaduct.demoapp.films.viaduct.mappers

import viaduct.api.context.ExecutionContext
import viaduct.api.grts.Film

/**
 * A builder class for constructing [Film] GraphQL objects from
 * [viaduct.demoapp.films.models.entities.Film] entities.
 *
 * @property ctx The execution context used for building the GraphQL object.
 */
class FilmBuilder(private val ctx: ExecutionContext) {
    fun build(film: viaduct.demoapp.films.models.entities.Film): Film =
        Film.Builder(ctx)
            .id(ctx.globalIDFor(Film.Reflection, film.id))
            .title(film.title)
            .episodeID(film.episodeID)
            .director(film.director)
            .producers(film.producers)
            .releaseDate(film.releaseDate)
            .created(film.created.toString())
            .edited(film.edited.toString())
            .build()
}
