package com.example.starwars.modules.filmography.films.resolvers

import com.example.starwars.filmography.resolverbases.FilmResolvers
import com.example.starwars.modules.filmography.characters.models.CharacterBuilder
import com.example.starwars.modules.filmography.characters.models.CharacterRepository
import com.example.starwars.modules.filmography.films.models.FilmCharactersRepository
import viaduct.api.Resolver
import viaduct.api.grts.Character

/**
 * Example of a relationship field resolver in the Film type.
 *
 * This resolver fetches the list of main characters appearing in a film.
 *
 * @resolver("fragment _ on Film { id }"): Fragment syntax for accessing film ID
 */
@Resolver("id")
class FilmCharactersResolver : FilmResolvers.Characters() {
    override suspend fun resolve(ctx: Context): List<Character?>? {
        val filmId = ctx.objectValue.getId().internalID

        return FilmCharactersRepository.findCharactersByFilmId(filmId).map {
            val character = CharacterRepository.findById(it) ?: throw IllegalArgumentException("Character with ID $it not found")
            CharacterBuilder(ctx).build(character)
        }
    }
}
