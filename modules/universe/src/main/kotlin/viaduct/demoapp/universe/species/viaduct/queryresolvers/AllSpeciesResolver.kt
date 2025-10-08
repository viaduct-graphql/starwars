package viaduct.demoapp.universe.species.viaduct.queryresolvers

import viaduct.api.Resolver
import viaduct.api.grts.Species
import viaduct.demoapp.universe.resolverbases.QueryResolvers
import viaduct.demoapp.universe.species.models.repository.SpeciesRepository
import viaduct.demoapp.universe.species.viaduct.mappers.SpeciesBuilder

const val DEFAULT_PAGE_SIZE = 10

/**
 * Resolver for the `allSpecies` query in the Star Wars GraphQL API.
 *
 * This resolver fetches a list of species, limited by the provided argument or a default page size.
 */
@Resolver
class AllSpeciesResolver : QueryResolvers.AllSpecies() {
    override suspend fun resolve(ctx: Context): List<Species?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val species = SpeciesRepository.findAll().take(limit)
        return species.map(SpeciesBuilder(ctx)::build)
    }
}
