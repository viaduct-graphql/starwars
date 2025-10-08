package viaduct.demoapp.universe.planets.viaduct.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Planet
import viaduct.demoapp.universe.NodeResolvers
import viaduct.demoapp.universe.planets.models.repository.PlanetsRepository
import viaduct.demoapp.universe.planets.viaduct.mappers.PlanetBuilder

/**
 * Node resolver for fetching a single planet node by its ID.
 */
@Resolver
class PlanetNodeResolver : NodeResolvers.Planet() {
    /**
     * Resolves a single planet node based on the provided context.
     *
     * @param ctx The context containing the ID and other request-related data.
     * @return A `Planet` object representing the resolved planet node.
     * @throws IllegalArgumentException if no planet is found with the given ID.
     */
    override suspend fun resolve(ctx: Context): Planet {
        // The context autofill internal ID.
        val stringId = ctx.id.internalID

        // Fetch the planet from the repository using the extracted ID.
        val planet = PlanetsRepository.findById(stringId)
            ?: throw IllegalArgumentException("Planet with ID $stringId not found")

        // Build and return the viaduct `Planet` object using the PlanetBuilder.
        return PlanetBuilder(ctx).build(planet)
    }
}
