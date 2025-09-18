package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Starship
import viaduct.demoapp.starships.NodeResolvers
import viaduct.demoapp.starwars.data.StarshipsData

/**
 * Node resolver for Starship entities
 */
@Resolver
class StarshipNodeResolver : NodeResolvers.Starship() {
    override suspend fun resolve(ctx: Context): Starship {
        val stringId = ctx.id.internalID
        val starship = StarshipsData.starships.find { it.id == stringId }
            ?: throw IllegalArgumentException("Starship with ID $stringId not found")

        return Starship.Builder(ctx)
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
