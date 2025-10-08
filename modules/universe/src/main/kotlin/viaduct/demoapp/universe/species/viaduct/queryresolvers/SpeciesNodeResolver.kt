package viaduct.demoapp.universe.species.viaduct.queryresolvers

import viaduct.api.Resolver
import viaduct.demoapp.universe.NodeResolvers
import viaduct.demoapp.universe.species.models.repository.SpeciesRepository
import viaduct.demoapp.universe.species.viaduct.mappers.SpeciesBuilder

/**
 * Node resolver for the Species type in the Star Wars GraphQL API.
 *
 * This resolver handles fetching a Species by its global ID.
 */
@Resolver
class SpeciesNodeResolver : NodeResolvers.Species() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Species {
        val stringId = ctx.id.internalID
        val species = SpeciesRepository.findById(stringId)
            ?: throw IllegalArgumentException("Species with ID $stringId not found")

        return SpeciesBuilder(ctx).build(species)
    }
}
