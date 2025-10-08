plugins {
    `java-library`
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.viaduct.module)
}

viaductModule {
    modulePackageSuffix.set("universe")
}

dependencies {
    implementation(libs.spring.context)
}
