package viaduct.demoapp.characters.viaduct.mutations

import viaduct.api.Resolver
import viaduct.api.grts.AddCharacterToFilmPayload
import viaduct.demoapp.characters.models.repository.CharacterFilmsRepository
import viaduct.demoapp.characters.models.repository.CharacterRepository
import viaduct.demoapp.characters.viaduct.mappers.CharacterBuilder
import viaduct.demoapp.filmography.resolverbases.MutationResolvers
import viaduct.demoapp.films.models.repository.FilmCharactersRepository
import viaduct.demoapp.films.models.repository.FilmsRepository
import viaduct.demoapp.films.viaduct.mappers.FilmBuilder

/**
 * Mutation resolvers for the Star Wars GraphQL API.
 *
 * The Mutation type demonstrates the @scope directive which restricts schema access
 * to specific tenants or contexts. All resolvers here are scoped to "starwars".
 */
@Resolver
class AddCharacterToFilmResolvers : MutationResolvers.AddCharacterToFilm() {
    override suspend fun resolve(ctx: Context): AddCharacterToFilmPayload? {
        // Extract input arguments
        val input = ctx.arguments.input
        val filmId = input.inputData["filmId"]?.toString() ?: throw IllegalArgumentException("Film ID is required")
        val characterId = input.inputData["characterId"]?.toString() ?: throw IllegalArgumentException("Character ID is required")

        // Early validation to ensure both character and film exist
        CharacterRepository.findById(characterId)
            ?: throw IllegalArgumentException("Character with ID $characterId not found")

        FilmsRepository.findFilmById(filmId)
            ?: throw IllegalArgumentException("Film with ID $filmId not found")

        // Add character to film in both repositories to maintain consistency
        CharacterFilmsRepository.addCharacterToFilm(characterId, filmId)
        FilmCharactersRepository.addCharacterToFilm(filmId, characterId)

        // Fetch updated entities
        val character = CharacterRepository.findById(characterId)
            ?: throw IllegalArgumentException("Character with ID $characterId not found")

        val film = FilmsRepository.findFilmById(filmId)
            ?: throw IllegalArgumentException("Film with ID $filmId not found")

        // From updated entities, build GraphQL objects to build the payload
        val filmGrt = FilmBuilder(ctx).build(film)
        val characterGrt = CharacterBuilder(ctx).build(character)

        return AddCharacterToFilmPayload.Builder(ctx)
            .film(filmGrt)
            .character(characterGrt)
            .build()
    }
}
