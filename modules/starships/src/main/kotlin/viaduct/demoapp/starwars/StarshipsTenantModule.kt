package viaduct.demoapp.starwars

import viaduct.api.TenantModule

/**
 * Starships tenant module configuration.
 *
 * This tenant module provides starship-related GraphQL types and resolvers,
 * extending the base StarWars schema with starship functionality.
 */
class StarshipsTenantModule : TenantModule {
    override val metadata = mapOf(
        "name" to "StarshipsTenantModule",
        "description" to "A tenant module for starships in the Star Wars demo application.",
        "scope" to "default"
    )
}
