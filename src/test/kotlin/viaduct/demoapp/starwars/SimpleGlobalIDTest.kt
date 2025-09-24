package viaduct.demoapp.starwars

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import viaduct.api.grts.Character

/**
 * Simple test to debug GlobalID implementation
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleGlobalIDTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    private var port: Int = 0

    private val objectMapper = ObjectMapper()

    private fun executeGraphQLQuery(query: String): JsonNode {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = mapOf("query" to query)
        val entity = HttpEntity(request, headers)

        val response = restTemplate.postForEntity(
            "http://localhost:$port/graphql",
            entity,
            String::class.java
        )

        return objectMapper.readTree(response.body)
    }

    @Test
    fun `should return encoded GlobalID for character query`() {
        val encodedCharacterId = Character.Reflection.globalId("1")
        val query = """
            query {
              node(id: "$encodedCharacterId") {
                ... on Character {
                  id
                  name
                }
              }
            }
        """.trimIndent()

        val result = executeGraphQLQuery(query)

        // Verify the response structure
        val data = result.get("data")
        org.junit.jupiter.api.Assertions.assertNotNull(data, "Data should not be null")

        val character = data.get("node")
        org.junit.jupiter.api.Assertions.assertNotNull(character, "Character should not be null")

        // Verify the GlobalID is encoded properly using GlobalIDImpl
        val globalId = character.get("id").asText()
        val expectedGlobalId = Character.Reflection.globalId("1")
        org.junit.jupiter.api.Assertions.assertEquals(expectedGlobalId, globalId, "GlobalID should match expected encoded value")

        // Verify the name is correct
        val name = character.get("name").asText()
        org.junit.jupiter.api.Assertions.assertEquals("Luke Skywalker", name, "Name should match expected value")
    }
}
