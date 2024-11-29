import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.8"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.32"
    kotlin("plugin.spring") version "1.5.32"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.32"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.32"
    id("jacoco")
}

group = "xyz.crosswars"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

// Allow spring to extend classes with these annotations
allOpen {
    annotation("org.springframework.stereotype.Component")
    annotation("org.springframework.stereotype.Async")
    annotation("org.springframework.stereotype.Transactional")
    annotation("org.springframework.stereotype.Cacheable")
    annotation("org.springframework.stereotype.SpringBootTest")
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Springfox
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    // Google api
    implementation("com.google.api-client:google-api-client:1.32.1")

    // Database
    implementation("org.xerial:sqlite-jdbc:3.36.0.1")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.1")
    testImplementation("io.kotest:kotest-property-jvm:4.6.1")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("${buildDir}/jacoco/"))
    }
    doLast {
        println("file://${buildDir}/jacoco/index.html")
    }
    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it).matching {
            exclude(
                "com/tgt/profile/config/**",
                "com/tgt/profile/constants/**",
                "com/tgt/profile/exception/**",
                "com/tgt/profile/model/**",
                "com/tgt/profile/util/**",
                "com/tgt/profile/tokenmanager/**",
                "com/tgt/profile/ProfileAggregatorMain**"
            )
        }
    })
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            limit {
                minimum = BigDecimal(1.0)
            }
            // Exclude classes from coverage
            excludes = listOf(
                "com.tgt.profile.config.*",
                "com.tgt.profile.constants.*",
                "com.tgt.profile.exception.*",
                "com.tgt.profile.model.*",
                "com.tgt.profile.util.*",
                "com.tgt.profile.tokenmanager.*",
                "com.tgt.profile.webclient.IdentityWebClient.getByGuestId.lambda**",
                "com.tgt.profile.ProfileAggregatorMain**"
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    testLogging.setEvents(listOf("skipped", "failed"))
    testLogging.showExceptions = true
    testLogging.setExceptionFormat("full")
    testLogging.showCauses = true
    testLogging.showStackTraces = true
    testLogging.showStandardStreams = false
}

//tasks.check {
//    dependsOn(tasks.jacocoTestCoverageVerification)
//}
