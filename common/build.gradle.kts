plugins {
    id("java")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.25")
    compileOnly("org.jetbrains:annotations:26.0.2")
}