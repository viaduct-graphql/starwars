package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.api.grts.Planet
import viaduct.api.grts.Species
import viaduct.demoapp.starwars.data.StarWarsData

@Resolver("id")
class FilmCharactersResolver : viaduct.demoapp.starwars.resolverbases.FilmResolvers.Characters() {
    override suspend fun resolve(ctx: Context): List<Character?>? {
        val filmId = ctx.objectValue.getId().internalID
        val characterIds = StarWarsData.filmCharacterRelations[filmId] ?: emptyList()
        val characters = StarWarsData.characters.filter { it.id in characterIds.toSet() }
        return characters.map { c ->
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
class FilmPlanetsResolver : viaduct.demoapp.starwars.resolverbases.FilmResolvers.Planets() {
    override suspend fun resolve(ctx: Context): List<Planet?>? {
        val filmId = ctx.objectValue.getId().internalID
        val characterIds = StarWarsData.filmCharacterRelations[filmId] ?: emptyList()
        val homeworldIds = StarWarsData.characters
            .filter { it.id in characterIds.toSet() }
            .mapNotNull { it.homeworldId }
            .toSet()
        val planets = StarWarsData.planets.filter { it.id in homeworldIds }
        return planets.map { p ->
            Planet.Builder(ctx)
                .id(ctx.globalIDFor(Planet.Reflection, p.id))
                .name(p.name)
                .diameter(p.diameter)
                .rotationPeriod(p.rotationPeriod)
                .orbitalPeriod(p.orbitalPeriod)
                .gravity(p.gravity?.toDouble())
                .population(p.population?.toDouble())
                .climates(p.climates)
                .terrains(p.terrains)
                .surfaceWater(p.surfaceWater?.toDouble())
                .created(p.created.toString())
                .edited(p.edited.toString())
                .build()
        }
    }
}

@Resolver("id")
class FilmSpeciesResolver : viaduct.demoapp.starwars.resolverbases.FilmResolvers.Species() {
    override suspend fun resolve(ctx: Context): List<Species?>? {
        val filmId = ctx.objectValue.getId().internalID
        val characterIds = StarWarsData.filmCharacterRelations[filmId] ?: emptyList()
        val speciesIds = StarWarsData.characters
            .filter { it.id in characterIds.toSet() }
            .mapNotNull { it.speciesId }
            .toSet()
        val species = StarWarsData.species.filter { it.id in speciesIds }
        return species.map { s ->
            Species.Builder(ctx)
                .id(ctx.globalIDFor(Species.Reflection, s.id))
                .name(s.name)
                .classification(s.classification)
                .designation(s.designation)
                .averageHeight(s.averageHeight?.toDouble())
                .averageLifespan(s.averageLifespan)
                .eyeColors(s.eyeColors)
                .hairColors(s.hairColors)
                .language(s.language)
                .created(s.created.toString())
                .edited(s.edited.toString())
                .build()
        }
    }
}
