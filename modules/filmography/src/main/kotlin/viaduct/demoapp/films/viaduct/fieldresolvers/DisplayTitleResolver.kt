package viaduct.demoapp.films.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.filmography.resolverbases.FilmResolvers

/**
 * Shorthand fragment syntax example - delegates to the title field
 *
 * @resolver("title"): Shorthand fragment syntax that delegates resolution to another field.
 *                   This resolver will automatically fetch the "title" field and return its value.
 */
@Resolver("title")
class DisplayTitleResolver : FilmResolvers.DisplayTitle() {
    override suspend fun resolve(ctx: Context): String? {
        // Access the source Film from the context
        return ctx.objectValue.getTitle()
    }
}
