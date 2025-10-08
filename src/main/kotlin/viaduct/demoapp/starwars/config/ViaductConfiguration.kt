package viaduct.demoapp.starwars.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import viaduct.api.Resolver
import viaduct.service.BasicViaductFactory
import viaduct.service.SchemaRegistrationInfo
import viaduct.service.SchemaScopeInfo
import viaduct.service.TenantRegistrationInfo
import viaduct.service.api.Viaduct

const val SCHEMA_ID = "publicSchema"
const val SCHEMA_ID_WITH_EXTRAS = "publicSchemaWithExtras"

const val DEFAULT_SCOPE_ID = "default"
const val EXTRAS_SCOPE_ID = "extras"

/**
 *  Scans for all classes annotated with [Resolver] and registers them as Spring beans.
 */
class ResolverBeanDefinitionRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(
        importingClassMetadata: AnnotationMetadata,
        registry: BeanDefinitionRegistry
    ) {
        val scanner = ClassPathBeanDefinitionScanner(registry, false)

        // Add filter to include only classes annotated with @Resolver
        scanner.addIncludeFilter(AnnotationTypeFilter(Resolver::class.java))

        // Scan the base package where your resolvers are located
        scanner.scan("viaduct.demoapp")
    }
}

@Configuration
@Import(ResolverBeanDefinitionRegistrar::class)
class ViaductConfiguration {
    @Autowired
    lateinit var codeInjector: SpringTenantCodeInjector

    @Bean
    fun viaductService(): Viaduct =
        BasicViaductFactory.create(
            // Register two schemas: one with the "extras" scope and one without
            schemaRegistrationInfo = SchemaRegistrationInfo(
                scopes = listOf(
                    SchemaScopeInfo(SCHEMA_ID, setOf(DEFAULT_SCOPE_ID)),
                    SchemaScopeInfo(SCHEMA_ID_WITH_EXTRAS, setOf(DEFAULT_SCOPE_ID, EXTRAS_SCOPE_ID))
                ),
                packagePrefix = "viaduct.demoapp", // Scan the entire viaduct.demoapp package for graphqls resources
                resourcesIncluded = ".*\\.graphqls"
            ),
            // The list of tenenats that we want to support
            tenantRegistrationInfo = TenantRegistrationInfo(
                tenantPackagePrefix = "viaduct.demoapp", // Scan the entire viaduct.demoapp package for tenant-specific code
                tenantCodeInjector = codeInjector
            )
        )

    @Bean
    @Order(0)
    fun graphiQlRouterFunction(): RouterFunction<ServerResponse> {
        val resource = ClassPathResource("graphiql/index.html")
        return RouterFunctions.route()
            .GET("/graphiql") { _ ->
                ServerResponse.ok().body(resource)
            }
            .GET("/graphiql/") { _ ->
                ServerResponse.ok().body(resource)
            }
            .build()
    }
}
