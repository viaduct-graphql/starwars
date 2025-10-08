package viaduct.demoapp.starwars.rest

import graphql.ExecutionResult
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import viaduct.demoapp.starwars.config.DEFAULT_SCOPE_ID
import viaduct.demoapp.starwars.config.EXTRAS_SCOPE_ID
import viaduct.demoapp.starwars.config.SCHEMA_ID
import viaduct.demoapp.starwars.config.SCHEMA_ID_WITH_EXTRAS
import viaduct.service.api.ExecutionInput
import viaduct.service.api.Viaduct

// HTTP header to retrieve query scopes to apply (e.g., "extras")
const val SCOPES_HEADER = "X-Viaduct-Scopes"

const val QUERY_FIELD = "query"
const val VARIABLES_FIELD = "variables"

@RestController
class ViaductGraphQLController {
    @Autowired
    lateinit var viaduct: Viaduct

    @PostMapping("/graphql")
    suspend fun graphql(
        @RequestBody request: Map<String, Any>,
        @RequestHeader headers: HttpHeaders
    ): ResponseEntity<Map<String, Any>> {
        val scopes = parseScopes(headers)
        val schemaId = determineSchemaId(scopes)
        val executionInput = createExecutionInput(request, schemaId)

        val result = viaduct.executeAsync(executionInput).await()

        return ResponseEntity.status(statusCode(result)).body(result.toSpecification())
    }

    private fun parseScopes(headers: HttpHeaders): Set<String> {
        val scopesHeader = headers.getFirst(SCOPES_HEADER)
        return if (scopesHeader != null) {
            scopesHeader.split(",").map { it.trim() }.toSet()
        } else {
            setOf(DEFAULT_SCOPE_ID)
        }
    }

    private fun determineSchemaId(scopes: Set<String>): String {
        return if (scopes.contains(EXTRAS_SCOPE_ID)) {
            SCHEMA_ID_WITH_EXTRAS
        } else {
            SCHEMA_ID
        }
    }

    private fun createExecutionInput(
        request: Map<String, Any>,
        schemaId: String
    ): ExecutionInput {
        @Suppress("UNCHECKED_CAST")
        return ExecutionInput.create(
            schemaId = schemaId,
            operationText = request[QUERY_FIELD] as String,
            variables = (request[VARIABLES_FIELD] as? Map<String, Any>) ?: emptyMap(),
            requestContext = emptyMap<String, Any>(),
        )
    }

    private fun statusCode(result: ExecutionResult) =
        when {
            result.isDataPresent && result.errors.isNotEmpty() -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.OK
        }
}
