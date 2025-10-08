package viaduct.demoapp.universe.species.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.universe.resolverbases.SpeciesResolvers
import viaduct.demoapp.universe.species.models.repository.SpeciesRepository

/**
 * Resolver for `rarityLevel` field in Species.
 *
 * Returns the rarity level of the species, or null if none exists.
 */
@Resolver("id")
class RarityLevelResolver : SpeciesResolvers.RarityLevel() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = SpeciesRepository.findById(speciesId)

        return species?.extrasData?.rarityLevel
    }
}
