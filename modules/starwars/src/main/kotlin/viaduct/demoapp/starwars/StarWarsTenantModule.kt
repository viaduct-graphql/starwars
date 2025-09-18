package viaduct.demoapp.starwars

import viaduct.api.TenantModule

/**
 * Star Wars tenant module configuration.
 *
 * This demonstrates the @scope directive implementation - the entire tenant
 * is scoped to "starwars" context, meaning all queries, types, and resolvers
 * in this module are only available when accessing the starwars schema.
 */
class StarWarsTenantModule : TenantModule {
    override val metadata = mapOf(
        "name" to "StarWarsTenantModule",
        "description" to "A tenant module for the Star Wars demo application.",
        "scope" to "starwars"
    )
}
