package com.example.starwars.modules.universe.species.resolvers

import com.example.starwars.modules.universe.species.models.SpeciesRepository
import com.example.starwars.universe.resolverbases.SpeciesResolvers
import viaduct.api.Resolver

/**
 * Resolver for `rarityLevel` field in Species.
 *
 * Returns the rarity level of the species, or null if none exists.
 */
@Resolver("id")
class SpeciesRarityLevelResolver : SpeciesResolvers.RarityLevel() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = SpeciesRepository.findById(speciesId)

        return species?.extrasData?.rarityLevel
    }
}
