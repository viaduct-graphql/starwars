package viaduct.demoapp.universe.species.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.universe.resolverbases.SpeciesResolvers
import viaduct.demoapp.universe.species.models.repository.SpeciesRepository

/**
 * Resolver for `specialAbilities` field in Species.
 *
 * Returns a list of special abilities of the species, or null if none exist.
 */
@Resolver("id")
class SpecialAbilitiesResolver : SpeciesResolvers.SpecialAbilities() {
    override suspend fun resolve(ctx: Context): List<String?>? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = SpeciesRepository.findById(speciesId)

        return species?.extrasData?.specialAbilities
    }
}
