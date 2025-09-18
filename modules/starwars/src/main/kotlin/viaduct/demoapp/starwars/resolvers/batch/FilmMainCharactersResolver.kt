package viaduct.demoapp.starwars.resolvers.batch

import viaduct.api.FieldValue
import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.demoapp.starwars.data.StarWarsData
import viaduct.demoapp.starwars.resolverbases.FilmResolvers

/**
 * Demonstrates **Reverse Relationship Batching** for one-to-many relationships.
 *
 * ## Reverse vs Forward Relationships
 * - Forward: Character → Homeworld (many-to-one)
 * - Reverse: Film → Characters (one-to-many)
 *
 * ## Optimization Strategy
 * 1. Collect all needed character IDs across all films
 * 2. Build character objects once using deduplication
 * 3. Distribute pre-built objects to each film
 *
 * This prevents rebuilding the same character objects for multiple films.
 */
@Resolver(objectValueFragment = "fragment _ on Film { id }")
class FilmMainCharactersResolver : FilmResolvers.MainCharacters() {
    override suspend fun batchResolve(contexts: List<Context>): List<FieldValue<List<Character>>> {
        val filmIds = contexts.map { it.objectValue.getId().internalID }

        // Batch lookup film-character relationships
        val filmCharacterMap = StarWarsData.filmCharacterRelations
            .filterKeys { it in filmIds }

        // Batch build all needed character objects
        val allCharacterIds = filmCharacterMap.values.flatten().toSet()
        val charactersById = StarWarsData.characters
            .filter { it.id in allCharacterIds }
            .associateBy { it.id }

        return contexts.map { ctx ->
            val filmId = ctx.objectValue.getId().internalID
            val characterIds = filmCharacterMap[filmId] ?: emptyList()

            val characters = characterIds.mapNotNull { characterId ->
                charactersById[characterId]?.let { characterData ->
                    Character.Builder(ctx)
                        .id(ctx.globalIDFor(Character.Reflection, characterData.id))
                        .name(characterData.name)
                        .birthYear(characterData.birthYear)
                        .eyeColor(characterData.eyeColor)
                        .gender(characterData.gender)
                        .hairColor(characterData.hairColor)
                        .height(characterData.height)
                        .mass(characterData.mass?.toDouble())
                        .created(characterData.created.toString())
                        .edited(characterData.edited.toString())
                        .build()
                }
            }

            FieldValue.ofValue(characters)
        }
    }
}
