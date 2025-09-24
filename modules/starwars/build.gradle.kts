plugins {
    `java-library`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.viaduct.module)
}

viaductModule {
    modulePackageSuffix.set("starwars")
}

dependencies {
    implementation(libs.spring.context)
}
