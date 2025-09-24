plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.dependencyManagement)
    alias(libs.plugins.viaduct.application)
    jacoco
}

viaductApplication {
    grtPackageName.set("viaduct.api.grts")
    modulePackagePrefix.set("viaduct.demoapp")
}

dependencies {
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.reactor.core)
    implementation(libs.spring.boot.starter.graphql)
    implementation(libs.spring.boot.starter.web)

    runtimeOnly(project(":modules:starwars"))
    runtimeOnly(project(":modules:starships"))

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "junit")
    }
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.io.mockk.jvm)
    testImplementation(project(":modules:starwars"))
    testImplementation(project(":modules:starships"))
    testImplementation(libs.kotest.runner.junit)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.json)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED"
    )
}
