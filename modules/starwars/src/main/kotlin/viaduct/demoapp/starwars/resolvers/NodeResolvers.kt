package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.api.grts.Film
import viaduct.api.grts.Planet
import viaduct.api.grts.Species
import viaduct.api.grts.Vehicle
import viaduct.demoapp.starwars.NodeResolvers
import viaduct.demoapp.starwars.data.StarWarsData

/**
 * Concrete Node resolver implementations
 */

@Resolver
class CharacterNodeResolver : NodeResolvers.Character() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Character {
        val stringId = ctx.id.internalID
        val character = StarWarsData.characters.find { it.id == stringId }
            ?: throw IllegalArgumentException("Character with ID $stringId not found")

        return Character.Builder(ctx)
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
}

@Resolver
class FilmNodeResolver : NodeResolvers.Film() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Film {
        val filmId = ctx.id.internalID
        val film = StarWarsData.films.find { it.id == filmId }
            ?: throw IllegalArgumentException("Film with ID $filmId not found")

        return Film.Builder(ctx)
            .title(film.title)
            .episodeID(film.episodeID)
            .director(film.director)
            .producers(film.producers)
            .releaseDate(film.releaseDate)
            .openingCrawl(film.openingCrawl)
            .created(film.created.toString())
            .edited(film.edited.toString())
            .build()
    }
}

@Resolver
class PlanetNodeResolver : NodeResolvers.Planet() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Planet {
        val stringId = ctx.id.internalID
        val planet = StarWarsData.planets.find { it.id == stringId }
            ?: throw IllegalArgumentException("Planet with ID $stringId not found")

        return Planet.Builder(ctx)
            .name(planet.name)
            .diameter(planet.diameter)
            .rotationPeriod(planet.rotationPeriod)
            .orbitalPeriod(planet.orbitalPeriod)
            .gravity(planet.gravity?.toDouble())
            .population(planet.population?.toDouble())
            .climates(planet.climates)
            .terrains(planet.terrains)
            .surfaceWater(planet.surfaceWater?.toDouble())
            .created(planet.created.toString())
            .edited(planet.edited.toString())
            .build()
    }
}

@Resolver
class SpeciesNodeResolver : NodeResolvers.Species() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Species {
        val stringId = ctx.id.internalID
        val species = StarWarsData.species.find { it.id == stringId }
            ?: throw IllegalArgumentException("Species with ID $stringId not found")

        return Species.Builder(ctx)
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
}

@Resolver
class VehicleNodeResolver : NodeResolvers.Vehicle() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Vehicle {
        val stringId = ctx.id.internalID
        val vehicle = StarWarsData.vehicles.find { it.id == stringId }
            ?: throw IllegalArgumentException("Vehicle with ID $stringId not found")

        return Vehicle.Builder(ctx)
            .name(vehicle.name)
            .model(vehicle.model)
            .vehicleClass(vehicle.vehicleClass)
            .manufacturers(vehicle.manufacturers)
            .costInCredits(vehicle.costInCredits?.toDouble())
            .length(vehicle.length?.toDouble())
            .crew(vehicle.crew)
            .passengers(vehicle.passengers)
            .maxAtmospheringSpeed(vehicle.maxAtmospheringSpeed)
            .cargoCapacity(vehicle.cargoCapacity?.toDouble())
            .consumables(vehicle.consumables)
            .created(vehicle.created.toString())
            .edited(vehicle.edited.toString())
            .build()
    }
}
