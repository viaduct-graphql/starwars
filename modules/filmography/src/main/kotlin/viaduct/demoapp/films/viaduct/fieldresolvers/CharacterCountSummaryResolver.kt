package viaduct.demoapp.films.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.filmography.resolverbases.FilmResolvers
import viaduct.demoapp.films.models.repository.FilmCharactersRepository

/**
 * Example of a computed field resolver in the Film type.
 *
 * This resolver computes a summary string that includes the film title and the count of main characters
 *
 * @resolver("fragment _ on Film { id title }"): Fragment syntax for accessing film title
 */
@Resolver(
    """
    fragment _ on Film {
        id
        title
    }
    """
)
class CharacterCountSummaryResolver : FilmResolvers.CharacterCountSummary() {
    override suspend fun resolve(ctx: Context): String? {
        // Access the source Film from the context
        val film = ctx.objectValue
        val filmId = film.getId().internalID

        // Access character count from the relationship data
        val characterCount = FilmCharactersRepository.findCharactersByFilmId(filmId).size

        return "${film.getTitle()} features $characterCount main characters"
    }
}
