package viaduct.demoapp.universe.starships.viaduct.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Starship
import viaduct.demoapp.universe.resolverbases.QueryResolvers
import viaduct.demoapp.universe.starships.models.repository.StarshipsRepository
import viaduct.demoapp.universe.starships.viaduct.mappers.StarshipBuilder

const val DEFAULT_PAGE_SIZE = 10

/**
 * Resolver for the `allStarships` query in the Star Wars GraphQL API.
 *
 * This resolver fetches a list of starships, limited by the provided argument or a default page size.
 */
@Resolver
class AllStarshipsResolver : QueryResolvers.AllStarships() {
    override suspend fun resolve(ctx: Context): List<Starship?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val starships = StarshipsRepository.findAll().take(limit)
        return starships.map { starship -> StarshipBuilder(ctx).build(starship) }
    }
}
