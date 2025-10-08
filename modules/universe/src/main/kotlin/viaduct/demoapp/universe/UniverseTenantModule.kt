package viaduct.demoapp.universe

import viaduct.api.TenantModule

/**
 * Tenant module for starships in the Star Wars demo application.
 *
 */
class UniverseTenantModule : TenantModule {
    /**
     * Metadata for the Universe module.
     *
     * - **name**: The name of the module.
     * - **description**: A brief description of the module's purpose.
     * - **scope**: The context in which the module is scoped, in this case "default".
     */
    override val metadata = mapOf(
        "name" to "UniverseTenantModule",
        "description" to "A tenant module for starships in the Star Wars demo application.",
        "scope" to "default"
    )
}
