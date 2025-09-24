package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.api.grts.Planet
import viaduct.demoapp.starwars.data.StarWarsData
import viaduct.demoapp.starwars.resolverbases.SpeciesResolvers

@Resolver("id")
class SpeciesHomeworldResolver : SpeciesResolvers.Homeworld() {
    override suspend fun resolve(ctx: Context): Planet? {
        val species = ctx.objectValue
        val homeWorldId = StarWarsData.species.first { it.id == species.getId().internalID }.homeworldId
        val planetData = StarWarsData.planets.find { it.id == homeWorldId } ?: return null
        return Planet.Builder(ctx)
            .id(ctx.globalIDFor(Planet.Reflection, planetData.id))
            .name(planetData.name)
            .diameter(planetData.diameter)
            .rotationPeriod(planetData.rotationPeriod)
            .orbitalPeriod(planetData.orbitalPeriod)
            .gravity(planetData.gravity?.toDouble())
            .population(planetData.population?.toDouble())
            .climates(planetData.climates)
            .terrains(planetData.terrains)
            .surfaceWater(planetData.surfaceWater?.toDouble())
            .created(planetData.created.toString())
            .edited(planetData.edited.toString())
            .build()
    }
}

@Resolver("id")
class SpeciesPeopleResolver : SpeciesResolvers.People() {
    override suspend fun resolve(ctx: Context): List<Character?>? {
        val speciesId = ctx.objectValue.getId().internalID
        val people = StarWarsData.characters.filter { it.speciesId == speciesId }
        return people.map { c ->
            Character.Builder(ctx)
                .id(ctx.globalIDFor(Character.Reflection, c.id))
                .name(c.name)
                .birthYear(c.birthYear)
                .eyeColor(c.eyeColor)
                .gender(c.gender)
                .hairColor(c.hairColor)
                .height(c.height)
                .mass(c.mass?.toDouble())
                .created(c.created.toString())
                .edited(c.edited.toString())
                .build()
        }
    }
}

@Resolver("id")
class SpeciesCulturalNotesResolver : SpeciesResolvers.CulturalNotes() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = StarWarsData.species.find { it.id == speciesId }
        return species?.extrasData?.culturalNotes
    }
}

@Resolver("id")
class SpeciesRarityLevelResolver : SpeciesResolvers.RarityLevel() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = StarWarsData.species.find { it.id == speciesId }
        return species?.extrasData?.rarityLevel
    }
}

@Resolver("id")
class SpeciesSpecialAbilitiesResolver : SpeciesResolvers.SpecialAbilities() {
    override suspend fun resolve(ctx: Context): List<String?>? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = StarWarsData.species.find { it.id == speciesId }
        return species?.extrasData?.specialAbilities
    }
}

@Resolver("id")
class SpeciesTechnologicalLevelResolver : SpeciesResolvers.TechnologicalLevel() {
    override suspend fun resolve(ctx: Context): String? {
        val speciesGrt = ctx.objectValue
        val speciesId = speciesGrt.getId().internalID
        val species = StarWarsData.species.find { it.id == speciesId }
        return species?.extrasData?.technologicalLevel
    }
}
