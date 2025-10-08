package viaduct.demoapp.universe.starships.viaduct.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Starship
import viaduct.demoapp.universe.NodeResolvers
import viaduct.demoapp.universe.starships.models.repository.StarshipsRepository
import viaduct.demoapp.universe.starships.viaduct.mappers.StarshipBuilder

/**
 * Node resolver for the Starship type in the Star Wars GraphQL API.
 *
 * This resolver handles fetching a Starship by its global ID.
 */
@Resolver
class StarshipNodeResolver : NodeResolvers.Starship() {
    override suspend fun resolve(ctx: Context): Starship {
        val stringId = ctx.id.internalID
        val starship = StarshipsRepository.findById(stringId)
            ?: throw IllegalArgumentException("Starship with ID $stringId not found")

        return StarshipBuilder(ctx).build(starship)
    }
}
