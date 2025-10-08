package viaduct.demoapp.universe.species.viaduct.mappers

import viaduct.api.context.ExecutionContext
import viaduct.api.grts.Species

/**
 * A builder class for constructing [Species] GraphQL objects from
 * [viaduct.demoapp.universe.species.models.entities.Species] entities.
 *
 * @property ctx The execution context used for building the GraphQL object.
 */
class SpeciesBuilder(private val ctx: ExecutionContext) {
    fun build(species: viaduct.demoapp.universe.species.models.entities.Species): Species =
        Species.Builder(ctx)
            .id(ctx.globalIDFor(Species.Reflection, species.id))
            .name(species.name)
            .classification(species.classification)
            .designation(species.designation)
            .averageHeight(species.averageHeight?.toDouble())
            .averageLifespan(species.averageLifespan)
            .eyeColors(species.eyeColors)
            .hairColors(species.hairColors)
            .language(species.language)
            .created(species.created.toString())
            .edited(species.edited.toString())
            .build()
}
