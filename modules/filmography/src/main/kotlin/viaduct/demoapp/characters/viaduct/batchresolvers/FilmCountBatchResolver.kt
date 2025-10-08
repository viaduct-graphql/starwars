package viaduct.demoapp.characters.viaduct.batchresolvers

import viaduct.api.FieldValue
import viaduct.api.Resolver
import viaduct.demoapp.characters.models.repository.CharacterFilmsRepository
import viaduct.demoapp.filmography.resolverbases.CharacterResolvers

/**
 * **Simple Batch Aggregation** for counting operations example.
 *
 * ## When to Use
 * - Counting relationships (films per character, comments per post)
 * - Simple calculations (totals, averages)
 * - Operations that benefit from batching but don't require complex object building
 *
 * ## How batchResolve Works
 *
 * The `batchResolve()` method is called by the framework when multiple field requests
 * are collected for the same resolver. Instead of calling `resolve()` individually
 * for each character, the framework groups them together.
 *
 * ### What `contexts` Contains
 * Each `Context` in the list represents one field resolution request:
 * ```kotlin
 * // For query: allCharacters { filmCount }
 * contexts = [
 *     Context(Character(id="1")),  // Luke's filmCount request
 *     Context(Character(id="2")),  // Leia's filmCount request
 *     Context(Character(id="3"))   // Han's filmCount request
 * ]
 * ```
 *
 * Each context provides:
 * - `ctx.objectValue`: The Character object this field belongs to
 * - `ctx.arguments`: Any arguments passed to the filmCount field
 * - Framework data for building results
 *
 * ## Efficiency
 * Instead of N individual count operations, performs one batch lookup and maps results.
 */
@Resolver(objectValueFragment = "fragment _ on Character { id }")
class FilmCountBatchResolver : CharacterResolvers.FilmCount() {
    override suspend fun batchResolve(contexts: List<Context>): List<FieldValue<Int>> {
        // Extract all the character IDs from the contexts
        val characterIds = contexts.map { it.objectValue.getId().internalID }

        // Perform a single batch query to get film counts for all characters
        val filmCounts = characterIds.associateWith { characterId ->
            CharacterFilmsRepository.findFilmsByCharacterId(characterId).size
        }

        // Map the results back to the original contexts in the same order
        return contexts.map { ctx ->
            val characterId = ctx.objectValue.getId().internalID

            FieldValue.ofValue(filmCounts[characterId] ?: 0)
        }
    }
}
