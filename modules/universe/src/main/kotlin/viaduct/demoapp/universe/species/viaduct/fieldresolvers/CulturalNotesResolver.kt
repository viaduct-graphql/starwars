package viaduct.demoapp.universe.species.viaduct.fieldresolvers

import viaduct.api.Resolver
import viaduct.demoapp.universe.resolverbases.SpeciesResolvers
import viaduct.demoapp.universe.species.models.repository.SpeciesRepository

/**
 * Resolver for `culturalNotes` field in Species.
 *
 * Returns any additional cultural information about the species, or null if none exists.
 */
@Resolver("id")
class CulturalNotesResolver : SpeciesResolvers.CulturalNotes() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = SpeciesRepository.findById(speciesId)

        return species?.extrasData?.culturalNotes
    }
}
