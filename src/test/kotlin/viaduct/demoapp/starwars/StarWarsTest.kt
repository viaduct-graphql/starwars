package viaduct.demoapp.starwars

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StarWarsTest : StringSpec({
    "should start application context" {
        true shouldBe true
    }
})
