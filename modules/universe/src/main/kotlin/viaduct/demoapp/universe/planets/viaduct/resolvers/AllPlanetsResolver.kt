package viaduct.demoapp.universe.planets.viaduct.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Planet
import viaduct.demoapp.universe.planets.models.repository.PlanetsRepository
import viaduct.demoapp.universe.planets.viaduct.mappers.PlanetBuilder
import viaduct.demoapp.universe.resolverbases.QueryResolvers

const val DEFAULT_PAGE_SIZE = 10

/**
 * Resolver for fetching a list of planets.
 *
 * This class extends the `QueryResolvers.AllPlanets` base class and provides
 * the implementation for resolving all planets with an optional limit.
 */
@Resolver
class AllPlanetsResolver : QueryResolvers.AllPlanets() {
    override suspend fun resolve(ctx: Context): List<Planet?>? {
        // The limit comes from arguments, or we take the default.
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE

        // Fetch planets from the repository.
        val planets = PlanetsRepository.findAll().take(limit)

        // Map the fetched planets to the Viaduct generated class.
        return planets.map(PlanetBuilder(ctx)::build)
    }
}
