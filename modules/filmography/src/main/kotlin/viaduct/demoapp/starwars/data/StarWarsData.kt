package viaduct.demoapp.starwars.data

import java.time.Instant
import viaduct.api.context.ExecutionContext
import viaduct.api.grts.Planet
import viaduct.api.grts.Species

const val UNKNOWN_DIAMETER = 0

// TODO: Remove this file and load data from a viaduct query
object StarWarsData {
    data class Planet(
        val id: String,
        val name: String,
        val diameter: Int?,
        val rotationPeriod: Int?,
        val orbitalPeriod: Int?,
        val gravity: Float?,
        val population: Float?,
        val climates: List<String>,
        val terrains: List<String>,
        val surfaceWater: Float?,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now()
    )

    // Main characters from Star Wars
    val planets = listOf(
        Planet(
            id = "1",
            name = "Tatooine",
            diameter = 10465,
            rotationPeriod = 23,
            orbitalPeriod = 304,
            gravity = 1f,
            population = 200000f,
            climates = listOf("arid"),
            terrains = listOf("desert"),
            surfaceWater = 1f
        ),
        Planet(
            id = "2",
            name = "Alderaan",
            diameter = 12500,
            rotationPeriod = 24,
            orbitalPeriod = 364,
            gravity = 1f,
            population = 2000000000f,
            climates = listOf("temperate"),
            terrains = listOf("grasslands", "mountains"),
            surfaceWater = 40f
        ),
        Planet(
            id = "3",
            name = "Corellia",
            diameter = 11000,
            rotationPeriod = 25,
            orbitalPeriod = 329,
            gravity = 1f,
            population = 3000000000f,
            climates = listOf("temperate"),
            terrains = listOf("plains", "urban", "hills", "forests"),
            surfaceWater = 70f
        ),
        Planet(
            id = "4",
            name = "Stewjon",
            diameter = UNKNOWN_DIAMETER,
            rotationPeriod = null,
            orbitalPeriod = null,
            gravity = 1f,
            population = null,
            climates = listOf("temperate"),
            terrains = listOf("grass"),
            surfaceWater = null
        ),
        Planet(
            id = "5",
            name = "Earth",
            diameter = UNKNOWN_DIAMETER,
            rotationPeriod = 24,
            orbitalPeriod = 365,
            gravity = 9.8f,
            population = 8_000_000_000F,
            climates = listOf("temperate"),
            terrains = listOf("grass"),
            surfaceWater = 70F
        ),
        Planet(
            id = "6",
            name = "Kashyyyk",
            diameter = 12765,
            rotationPeriod = 26,
            orbitalPeriod = 381,
            gravity = 1f,
            population = 45000000f,
            climates = listOf("tropical"),
            terrains = listOf("jungle", "forest", "lakes"),
            surfaceWater = 60f
        )
    )

    data class Species(
        val id: String,
        val name: String,
        val classification: String?,
        val designation: String?,
        val averageHeight: Float?,
        val averageLifespan: Int?,
        val eyeColors: List<String>,
        val hairColors: List<String>,
        val language: String?,
        val homeworldId: String?,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now(),
        // Extra fields for "extras" scope
        val extrasData: SpeciesExtrasData = SpeciesExtrasData()
    )

    data class SpeciesExtrasData(
        val culturalNotes: String? = null,
        val rarityLevel: String? = null,
        val specialAbilities: List<String> = emptyList(),
        val technologicalLevel: String? = null
    )

    val species = listOf(
        Species(
            id = "1",
            name = "Human",
            classification = "mammal",
            designation = "sentient",
            averageHeight = 180f,
            averageLifespan = 120,
            eyeColors = listOf("brown", "blue", "green", "hazel", "grey", "amber"),
            hairColors = listOf("blonde", "brown", "black", "red"),
            language = "Galactic Basic",
            homeworldId = "5",
            extrasData = SpeciesExtrasData(
                culturalNotes = "Diverse species with strong adaptability and technological advancement",
                rarityLevel = "Common",
                specialAbilities = listOf("Force sensitivity (rare)", "Adaptability", "Innovation"),
                technologicalLevel = "Advanced"
            )
        ),
        Species(
            id = "2",
            name = "Wookiee",
            classification = "mammal",
            designation = "sentient",
            averageHeight = 210f,
            averageLifespan = 400,
            eyeColors = listOf("blue", "brown", "green"),
            hairColors = listOf("brown", "black"),
            language = "Shyriiwook",
            homeworldId = "5"
        )
    )
}

class PlanetBuilder(private val ctx: ExecutionContext) {
    fun build(planet: StarWarsData.Planet): Planet =
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

class SpeciesBuilder(private val ctx: ExecutionContext) {
    fun build(species: StarWarsData.Species): Species =
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
