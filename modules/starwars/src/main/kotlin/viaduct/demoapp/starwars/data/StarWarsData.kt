package viaduct.demoapp.starwars.data

import java.time.Instant
import viaduct.demoapp.starwars.Constants.UNKNOWN_DIAMETER

/**
 * In-memory data store for Star Wars characters and related entities.
 * This serves as the backing data source for the GraphQL API.
 */
object StarWarsData {
    data class Character(
        val id: String,
        val name: String,
        val birthYear: String?,
        val eyeColor: String?,
        val gender: String?,
        val hairColor: String?,
        val height: Int?,
        val mass: Float?,
        val homeworldId: String?,
        val speciesId: String?,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now()
    )

    data class Film(
        val id: String,
        val title: String,
        val episodeID: Int,
        val openingCrawl: String,
        val director: String,
        val producers: List<String>,
        val releaseDate: String,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now()
    )

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

    data class Vehicle(
        val id: String,
        val name: String,
        val model: String?,
        val vehicleClass: String?,
        val manufacturers: List<String>,
        val costInCredits: Float?,
        val length: Float?,
        val crew: String?,
        val passengers: String?,
        val maxAtmospheringSpeed: Int?,
        val cargoCapacity: Float?,
        val consumables: String?,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now()
    )

    // Main characters from Star Wars
    val characters = listOf(
        Character(
            id = "1",
            name = "Luke Skywalker",
            birthYear = "19BBY",
            eyeColor = "blue",
            gender = "male",
            hairColor = "blond",
            height = 172,
            mass = 77f,
            homeworldId = "1",
            speciesId = "1"
        ),
        Character(
            id = "2",
            name = "Princess Leia",
            birthYear = "19BBY",
            eyeColor = "brown",
            gender = "female",
            hairColor = "brown",
            height = 150,
            mass = 49f,
            homeworldId = "2",
            speciesId = "1"
        ),
        Character(
            id = "3",
            name = "Han Solo",
            birthYear = "29BBY",
            eyeColor = "brown",
            gender = "male",
            hairColor = "brown",
            height = 180,
            mass = 80f,
            homeworldId = "3",
            speciesId = "1"
        ),
        Character(
            id = "4",
            name = "Darth Vader",
            birthYear = "41.9BBY",
            eyeColor = "yellow",
            gender = "male",
            hairColor = "none",
            height = 202,
            mass = 136f,
            homeworldId = "1",
            speciesId = "1"
        ),
        Character(
            id = "5",
            name = "Obi-Wan Kenobi",
            birthYear = "57BBY",
            eyeColor = "blue-gray",
            gender = "male",
            hairColor = "auburn, white",
            height = 182,
            mass = 77f,
            homeworldId = "4",
            speciesId = "1"
        )
    )

    val films = listOf(
        Film(
            id = "1",
            title = "A New Hope",
            episodeID = 4,
            openingCrawl = """
                It is a period of civil war.
                Rebel spaceships, striking
                from a hidden base, have won
                their first victory against
                the evil Galactic Empire.
            """.trimIndent(),
            director = "George Lucas",
            producers = listOf("Gary Kurtz", "Rick McCallum"),
            releaseDate = "1977-05-25"
        ),
        Film(
            id = "2",
            title = "The Empire Strikes Back",
            episodeID = 5,
            openingCrawl = """
                It is a dark time for the
                Rebellion. Although the Death
                Star has been destroyed,
                Imperial troops have driven the
                Rebel forces from their hidden
                base and pursued them across
                the galaxy.
            """.trimIndent(),
            director = "Irvin Kershner",
            producers = listOf("Gary Kurtz"),
            releaseDate = "1980-05-17"
        ),
        Film(
            id = "3",
            title = "Return of the Jedi",
            episodeID = 6,
            openingCrawl = """
                Luke Skywalker has returned to
                his home planet of Tatooine in
                an attempt to rescue his
                friend Han Solo from the
                clutches of the vile gangster
                Jabba the Hutt.
            """.trimIndent(),
            director = "Richard Marquand",
            producers = listOf("Howard G. Kazanjian", "George Lucas", "Rick McCallum"),
            releaseDate = "1983-05-25"
        )
    )

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
        )
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
        )
    )

    val vehicles = listOf(
        Vehicle(
            id = "1",
            name = "Speeder bike",
            model = "74-Z speeder bike",
            vehicleClass = "speeder",
            manufacturers = listOf("Aratech Repulsor Company"),
            costInCredits = 8000f,
            length = 3f,
            crew = "1",
            passengers = "1",
            maxAtmospheringSpeed = 360,
            cargoCapacity = 4f,
            consumables = "1 day"
        )
    )

    // Relationship mappings
    val characterFilmRelations = mapOf(
        "1" to listOf("1", "2", "3"), // Luke in all three films
        "2" to listOf("1", "2", "3"), // Leia in all three films
        "3" to listOf("1", "2", "3"), // Han in all three films
        "4" to listOf("1", "2", "3"), // Vader in all three films
        "5" to listOf("1", "2", "3") // Obi-Wan in all three films
    )

    val filmCharacterRelations = mapOf(
        "1" to listOf("1", "2", "3", "4", "5"), // A New Hope characters
        "2" to listOf("1", "2", "3", "4", "5"), // Empire characters
        "3" to listOf("1", "2", "3", "4", "5") // Return of the Jedi characters
    )
}
