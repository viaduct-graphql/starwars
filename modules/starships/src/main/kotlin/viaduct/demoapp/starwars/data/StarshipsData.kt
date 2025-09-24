package viaduct.demoapp.starwars.data

import java.time.Instant

/**
 * In-memory data store for Star Wars starships.
 * This serves as the backing data source for the starships GraphQL API.
 */
object StarshipsData {
    data class Starship(
        val id: String,
        val name: String,
        val model: String?,
        val starshipClass: String?,
        val manufacturers: List<String>,
        val costInCredits: Float?,
        val length: Float?,
        val crew: String?,
        val passengers: String?,
        val maxAtmospheringSpeed: Int?,
        val hyperdriveRating: Float?,
        val mglt: Int?,
        val cargoCapacity: Float?,
        val consumables: String?,
        val created: Instant = Instant.now(),
        val edited: Instant = Instant.now()
    )

    val starships = listOf(
        Starship(
            id = "1",
            name = "Millennium Falcon",
            model = "YT-1300 light freighter",
            starshipClass = "Light freighter",
            manufacturers = listOf("Corellian Engineering Corporation"),
            costInCredits = 100000f,
            length = 34.37f,
            crew = "4",
            passengers = "6",
            maxAtmospheringSpeed = 1050,
            hyperdriveRating = 0.5f,
            mglt = 75,
            cargoCapacity = 100000f,
            consumables = "2 months"
        ),
        Starship(
            id = "2",
            name = "X-wing",
            model = "T-65 X-wing",
            starshipClass = "Starfighter",
            manufacturers = listOf("Incom Corporation"),
            costInCredits = 149999f,
            length = 12.5f,
            crew = "1",
            passengers = "0",
            maxAtmospheringSpeed = 1050,
            hyperdriveRating = 1.0f,
            mglt = 100,
            cargoCapacity = 110f,
            consumables = "1 week"
        )
    )

    // Pilot relations - mapping starship IDs to character IDs who pilot them
    val starshipPilotRelations = mapOf(
        "1" to listOf("3"), // Millennium Falcon piloted by Han Solo
        "2" to listOf("1") // X-wing piloted by Luke Skywalker
    )
}
