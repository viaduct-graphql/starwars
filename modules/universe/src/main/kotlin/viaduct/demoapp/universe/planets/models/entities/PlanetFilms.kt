package viaduct.demoapp.universe.planets.models.entities

/**
 * Entity class representing the association between a film and a planet in the Star Wars universe.
 */
data class PlanetFilms(
    val filmId: String,
    val planetId: String,
)
