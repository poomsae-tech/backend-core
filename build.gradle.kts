import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
}

group = "ru.poomsae"

version = "0.0.1-SNAPSHOT"

description = "Core backend monolith for Poomsae Tech"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }

repositories { mavenCentral() }

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    val envFile = file(".env")

    if (envFile.exists()) {
        envFile
            .readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .map { it.split("=", limit = 2) }
            .forEach { (key, value) -> environment(key, value) }
    }
}

tasks.register("createMigration") {

    group = "database"
    description = "Creates a new Flyway migration. Usage: ./gradlew createMigration -Pmigration=init_users"

    doLast {

        if (!project.hasProperty("migration")) {
            throw GradleException("Provide migration name: -Pmigration=your_migration_name")
        }

        val migrationName = project.property("migration").toString()

        val cleanName = migrationName
            .lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "")

        val timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

        val dir = file("src/main/resources/db/migration")
        dir.mkdirs()

        val file = file("$dir/V${timestamp}__${cleanName}.sql")

        file.writeText(
            """
            -- Migration: $cleanName
            -- Created at: $timestamp
            
            
            """.trimIndent()
        )

        println("Created: ${file.path}")
    }
}