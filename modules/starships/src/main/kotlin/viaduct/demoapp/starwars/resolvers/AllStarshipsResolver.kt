package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Starship
import viaduct.demoapp.starships.resolverbases.QueryResolvers
import viaduct.demoapp.starwars.Constants.DEFAULT_PAGE_SIZE
import viaduct.demoapp.starwars.data.StarshipsData

/**
 * Query resolvers for the Starships GraphQL API.
 * These resolvers handle starship-related queries.
 */

@Resolver
class AllStarshipsResolver : QueryResolvers.AllStarships() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Starship?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val starships = StarshipsData.starships.take(limit)
        return starships.map { starship ->
            Starship.Builder(ctx)
                .id(ctx.globalIDFor(Starship.Reflection, starship.id))
                .name(starship.name)
                .model(starship.model)
                .starshipClass(starship.starshipClass)
                .manufacturers(starship.manufacturers)
                .costInCredits(starship.costInCredits?.toDouble())
                .length(starship.length?.toDouble())
                .crew(starship.crew)
                .passengers(starship.passengers)
                .maxAtmospheringSpeed(starship.maxAtmospheringSpeed)
                .hyperdriveRating(starship.hyperdriveRating?.toDouble())
                .MGLT(starship.mglt)
                .cargoCapacity(starship.cargoCapacity?.toDouble())
                .consumables(starship.consumables)
                .created(starship.created.toString())
                .edited(starship.edited.toString())
                .build()
        }
    }
}
