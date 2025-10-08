package viaduct.demoapp.universe.vehicles

import viaduct.api.TenantModule

/**
 * Star Wars Vehicles tenant module configuration.
 *
 * This demonstrates the @scope directive implementation - the entire tenant
 * is scoped to "starwars" context, meaning all queries, types, and resolvers
 * in this module are only available when accessing the starwars schema.
 */
class VehicleTenantModule : TenantModule {
    override val metadata = mapOf(
        "name" to "VehicleTenantModule",
        "description" to "A tenant module of Vehicles for the Star Wars demo application.",
        "scope" to "starwars"
    )
}
