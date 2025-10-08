package viaduct.demoapp.universe.species.models.entities

import java.time.Instant

/**
 * Data class representing extra data for a species, used in the "extras" scope.
 *
 * @property relatedCharacterIds List of character IDs related to this species.
 * @property relatedFilmIds List of film IDs related to this species.
 */
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
