package viaduct.demoapp.universe.planets.viaduct.mappers

import viaduct.api.context.ExecutionContext
import viaduct.api.grts.Planet

/**
 * Builder class for mapping viaduct generated Planet objects from Planet entity data.
 */
class PlanetBuilder(private val ctx: ExecutionContext) {
    fun build(planet: viaduct.demoapp.universe.planets.models.entities.Planet): Planet =
        Planet.Builder(ctx)
            .id(ctx.globalIDFor(Planet.Reflection, planet.id))
            .name(planet.name)
            .diameter(planet.diameter)
            .rotationPeriod(planet.rotationPeriod)
            .orbitalPeriod(planet.orbitalPeriod)
            .gravity(planet.gravity?.toDouble())
            .population(planet.population?.toDouble())
            .surfaceWater(planet.surfaceWater?.toDouble())
            .created(planet.created.toString())
            .edited(planet.edited.toString())
            .build()
}
