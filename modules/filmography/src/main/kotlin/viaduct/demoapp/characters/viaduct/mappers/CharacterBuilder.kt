package viaduct.demoapp.characters.viaduct.mappers

import viaduct.api.context.ExecutionContext
import viaduct.api.grts.Character

/**
 * A builder class for constructing [Character] GraphQL objects from
 * [viaduct.demoapp.characters.models.entities.Character] entities.
 *
 * @property ctx The execution context used for building the GraphQL object.
 */
class CharacterBuilder(private val ctx: ExecutionContext) {
    fun build(character: viaduct.demoapp.characters.models.entities.Character): Character =
        Character.Builder(ctx)
            .id(ctx.globalIDFor(Character.Reflection, character.id))
            .name(character.name)
            .birthYear(character.birthYear)
            .eyeColor(character.eyeColor)
            .gender(character.gender)
            .hairColor(character.hairColor)
            .height(character.height)
            .mass(character.mass?.toDouble())
            .created(character.created.toString())
            .edited(character.edited.toString())
            .build()
}
