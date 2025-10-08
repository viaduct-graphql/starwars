package viaduct.demoapp

import viaduct.api.TenantModule

/**
 * Represents the Filmography tenant module for the Star Wars demo application.
 *
 * Filmography includes all queries, types, and resolvers related to films and
 * characters within the Star Wars universe.
 */
class FilmographyTenantModule : TenantModule {
    /**
     * Metadata for the Filmography module.
     *
     * - **name**: The name of the module.
     * - **description**: A brief description of the module's purpose.
     * - **scope**: The context in which the module is scoped, in this case "default".
     */
    override val metadata = mapOf(
        "name" to "FilmographyTenantModule",
        "description" to "A tenant module for the Star Wars demo application.",
        "scope" to "default"
    )
}
