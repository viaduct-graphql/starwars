package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.api.grts.Film
import viaduct.api.grts.Planet
import viaduct.api.grts.Species
import viaduct.api.grts.Vehicle
import viaduct.demoapp.starwars.Constants.DEFAULT_PAGE_SIZE
import viaduct.demoapp.starwars.data.StarWarsData
import viaduct.demoapp.starwars.resolverbases.QueryResolvers

/**
 * Query resolvers for the Star Wars GraphQL API.
 *
 * The Query type demonstrates the @scope directive which restricts schema access
 * to specific tenants or contexts. All resolvers here are scoped to "starwars".
 */

/**
 * @resolver directive: Custom field resolution for Query.searchCharacter
 * @oneOf directive: The search input uses @oneOf directive, ensuring exactly one
 *                  search criteria is provided. This demonstrates input validation
 *                  where only one of byName, byId, or byBirthYear can be specified.
 */
@Resolver
class SearchCharacterResolver : QueryResolvers.SearchCharacter() {
    override suspend fun resolve(ctx: Context): viaduct.api.grts.Character? {
        val search = ctx.arguments.search
        val byName = search.byName
        val byId = search.byId
        val byBirthYear = search.byBirthYear

        val character = when {
            byName != null -> StarWarsData.characters.find { it.name.contains(byName, ignoreCase = true) }
            byId != null -> StarWarsData.characters.find { it.id == byId.internalID }
            byBirthYear != null -> StarWarsData.characters.find { it.birthYear == byBirthYear }
            else -> null
        }

        return if (character != null) {
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
        } else {
            null
        }
    }
}

/**
 * @resolver directive: Custom field resolution for Query.allCharacters
 * @backingData directive: Uses "starwars.query.AllCharacters" backing data class for pagination
 * @scope directive: This query is scoped to ["starwars"] tenant only
 */
@Resolver
class AllCharactersResolver : QueryResolvers.AllCharacters() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Character?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val characters = StarWarsData.characters.take(limit)

        // Convert StarWarsData.Character objects to Character objects
        return characters.map { character ->
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
    }
}

/**
 * @resolver directive: Custom field resolution for Query.allFilms
 * @backingData directive: Uses "starwars.query.AllFilms" backing data class for pagination
 * @scope directive: This query is scoped to ["starwars"] tenant only
 */
@Resolver
class AllFilmsResolver : QueryResolvers.AllFilms() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Film?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val films = StarWarsData.films.take(limit)

        // Convert StarWarsData.Film objects to Film objects
        return films.map { film ->
            Film.Builder(ctx)
                .id(ctx.globalIDFor(Film.Reflection, film.id))
                .title(film.title)
                .episodeID(film.episodeID)
                .director(film.director)
                .producers(film.producers)
                .releaseDate(film.releaseDate)
                .created(film.created.toString())
                .edited(film.edited.toString())
                .openingCrawl(film.openingCrawl)
                .build()
        }
    }
}

@Resolver
class AllPlanetsResolver : QueryResolvers.AllPlanets() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Planet?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val planets = StarWarsData.planets.take(limit)
        return planets.map { planet ->
            Planet.Builder(ctx)
                .id(ctx.globalIDFor(Planet.Reflection, planet.id))
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
}

@Resolver
class AllSpeciesResolver : QueryResolvers.AllSpecies() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Species?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val species = StarWarsData.species.take(limit)
        return species.map { speciesItem ->
            Species.Builder(ctx)
                .id(ctx.globalIDFor(Species.Reflection, speciesItem.id))
                .name(speciesItem.name)
                .classification(speciesItem.classification)
                .designation(speciesItem.designation)
                .averageHeight(speciesItem.averageHeight?.toDouble())
                .averageLifespan(speciesItem.averageLifespan)
                .eyeColors(speciesItem.eyeColors)
                .hairColors(speciesItem.hairColors)
                .language(speciesItem.language)
                .created(speciesItem.created.toString())
                .edited(speciesItem.edited.toString())
                .build()
        }
    }
}

@Resolver
class AllVehiclesResolver : QueryResolvers.AllVehicles() {
    override suspend fun resolve(ctx: Context): List<viaduct.api.grts.Vehicle?>? {
        val limit = ctx.arguments.limit ?: DEFAULT_PAGE_SIZE
        val vehicles = StarWarsData.vehicles.take(limit)
        return vehicles.map { vehicle ->
            Vehicle.Builder(ctx)
                .id(ctx.globalIDFor(Vehicle.Reflection, vehicle.id))
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
}
