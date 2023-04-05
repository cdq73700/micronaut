plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.3"
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("jacoco")
    id("com.github.jk1.dependency-license-report") version "2.1"
}

version = "0.1"
group = "backend"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.data:micronaut-data-jdbc:3.8.6")
    implementation("io.micronaut.serde:micronaut-serde-jackson:1.5.2")
    implementation("io.micronaut.flyway:micronaut-flyway:5.5.0")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari:4.8.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("javax:javaee-web-api:8.0.1")
    implementation("io.micronaut.openapi:micronaut-openapi:4.8.5")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.9")

    // runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
    testImplementation("org.powermock:powermock-module-junit4:2.0.9")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

application {
    mainClass.set("backend.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}
jacoco {
    toolVersion = "0.8.8"
}
licenseReport {
    outputDir = "$buildDir/tmp/kapt3/classes/main/META-INF/licenses"
}

tasks {
    compileKotlin {
        dependsOn("ktlintFormat")
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        dependsOn("ktlintFormat")
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    test {
        finalizedBy(jacocoTestReport)
        reports.html.destination = file("$buildDir/tmp/kapt3/classes/main/META-INF/test")
    }
    jacocoTestReport {
        dependsOn(test)
        finalizedBy(build)
        reports {
            xml.required.set(false)
            csv.required.set(false)
            html.outputLocation.set(file("$buildDir/tmp/kapt3/classes/main/META-INF/jacoco"))
        }
    }
    dokkaHtml.configure {
        finalizedBy(build)
        outputDirectory.set(file("$buildDir/tmp/kapt3/classes/main/META-INF/dokka"))
    }
    generateLicenseReport {
        finalizedBy(build)
    }

    build {
        dependsOn(dokkaHtml)
        dependsOn(jacocoTestReport)
        dependsOn(generateLicenseReport)
    }
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("backend.*")
    }
}
