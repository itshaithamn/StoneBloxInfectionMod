group = "io.github.itshaihtamn.infection"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.34.1")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
    }
}

tasks.test {
    useJUnitPlatform()
}