package viaduct.demoapp.starwars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["viaduct.demoapp"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
