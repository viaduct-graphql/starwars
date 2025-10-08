package viaduct.demoapp.universe.planets.viaduct.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.demoapp.universe.planets.models.repository.PlanetsResidentsRepository
import viaduct.demoapp.universe.resolverbases.PlanetResolvers

/**
 * Resolver to fetch the residents of a planet.
 * It retrieves characters whose homeworld matches the planet's ID.
 */
@Resolver("id")
class PlanetResidentsResolver : PlanetResolvers.Residents() {
    /**
     * Resolves the list of residents for a given planet based on the context.
     *
     * @param ctx The context containing the planet information.
     * @return A list of viaduct `Character` objects who are residents of the planet, or null if none are found.
     */
    override suspend fun resolve(ctx: Context): List<Character>? {
        // Related Planet ID is stored in the ctx object value.
        val planetId = ctx.objectValue.getId().internalID

        // Fetch residents associated with the planet from the repository.
        val residents = PlanetsResidentsRepository.findResidentsByPlanetId(planetId)

        return residents.map {
            // Create a global ID for the Character using its internal ID.
            val globalID = ctx.globalIDFor(Character.Reflection, it.characterId)

            // Request Viaduct to resolve the Character node using the global ID.
            ctx.nodeFor(globalID)
        }
    }
}
