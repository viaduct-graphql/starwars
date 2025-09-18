package viaduct.demoapp.starwars.resolvers.batch

import viaduct.api.FieldValue
import viaduct.api.Resolver
import viaduct.demoapp.starwars.data.StarWarsData
import viaduct.demoapp.starwars.resolverbases.CharacterResolvers

/**
 * Demonstrates **Multi-Source Batch Resolution** for complex data combination.
 *
 * ## Features
 * - Combines data from multiple sources (characters, films, planets, species)
 * - Uses deduplication to prevent duplicate lookups
 * - Efficient fragment declaration for required fields
 *
 * ## Fragment Strategy
 * Includes fields accessed directly (`name`, `birthYear`) plus `id` for lookups.
 * Other data fetched through batch operations.
 */
@Resolver(objectValueFragment = "fragment _ on Character { id name birthYear }")
class CharacterRichSummaryResolver : CharacterResolvers.RichSummary() {
    override suspend fun batchResolve(contexts: List<Context>): List<FieldValue<String>> {
        val characterIds = contexts.map { it.objectValue.getId().internalID }

        val charactersById = StarWarsData.characters
            .filter { it.id in characterIds }
            .associateBy { it.id }

        val filmCounts = characterIds.associateWith { characterId ->
            StarWarsData.characterFilmRelations[characterId]?.size ?: 0
        }

        // Batch lookup homeworld names
        val homeworldIds = charactersById.values.mapNotNull { it.homeworldId }.toSet()
        val planetNames = StarWarsData.planets
            .filter { it.id in homeworldIds }
            .associateBy({ it.id }, { it.name })

        return contexts.map { ctx ->
            val character = ctx.objectValue
            val characterId = character.getId().internalID
            val characterData = charactersById[characterId]

            val name = character.getName() ?: "Unknown"
            val birthYear = character.getBirthYear() ?: "Unknown"
            val homeworldName = characterData?.homeworldId?.let { planetNames[it] } ?: "Unknown world"
            val filmCount = filmCounts[characterId] ?: 0

            val summary = "$name ($birthYear) from $homeworldName, appears in $filmCount films"
            FieldValue.ofValue(summary)
        }
    }
}
