package viaduct.demoapp.universe.planets.models.entities

/**
 * Entity class representing the association between a character and a planet in the Star Wars universe.
 */
data class PlanetCharacter(
    val characterId: String,
    val planetId: String,
)
