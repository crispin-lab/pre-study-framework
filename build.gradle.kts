import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask
import org.jmailen.gradle.kotlinter.tasks.InstallPrePushHookTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jmailen.kotlinter") version "4.3.0"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.epages.restdocs-api-spec") version "0.18.2"
}

group = "com.crispinlab"
version = "0.0.1-SNAPSHOT"
val kotlinSnowflakeVersion = "1.0.1"
val springSecurityCryptoVersion = "6.5.0"
val nimbusJWTVersion = "10.3"
val restdocsSpecMockMvcVersion = "0.18.2"
val restAssuredVersion = "5.4.0"
val kotlinCoroutineVersion = "1.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.crispindeity:kotlin-snowflake:$kotlinSnowflakeVersion")
    implementation(
        "org.springframework.security:spring-security-crypto:$springSecurityCryptoVersion"
    )
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusJWTVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")
    runtimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:$restdocsSpecMockMvcVersion")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutineVersion")
}

if (!rootProject.extra.has("install-git-hooks")) {
    rootProject.extra["install-git-hooks"] = true

    val preCommit: TaskProvider<InstallPreCommitHookTask> =
        project.rootProject.tasks.register(
            "installPreCommitHook",
            InstallPreCommitHookTask::class
        ) {
            group = "build setup"
            description = "Installs Kotlinter Git pre-commit hook"
        }

    val prePush: TaskProvider<InstallPrePushHookTask> =
        project.rootProject.tasks.register(
            "installPrePushHook",
            InstallPrePushHookTask::class
        ) {
            group = "build setup"
            description = "Installs Kotlinter Git pre-push hook"
        }

    project.rootProject.tasks.named("installPrePushHook").configure {
        doLast {
            val hookFile = File(project.rootDir, ".git/hooks/pre-push")
            if (hookFile.exists()) {
                hookFile.writeText(
                    """
                    #!/bin/sh
                    set -e

                    GRADLEW=/Users/crispin/Documents/personal/git/pre-study-framework/gradlew
                    
                    ${'$'}GRADLEW test

                    if ! ${'$'}GRADLEW lintKotlin ; then
                        echo 1>&2 "\nlintKotlin found problems, running formatKotlin; commit the result and re-push"
                        ${'$'}GRADLEW formatKotlin
                        exit 1
                    fi
                    """.trimIndent()
                )
                hookFile.setExecutable(true)
            }
        }
    }

    project.rootProject.tasks.named("prepareKotlinBuildScriptModel") {
        dependsOn(preCommit, prePush)
    }
}

openapi3 {
    setServer("http://localhost:8080")
    title = "Pre Study"
    version = "1.0.0"
    description = "API Documents"
    format = "yaml"
}

tasks.register<Copy>("copySwaggerSpec") {
    description = "copy openapi3 document"
    group = JavaBasePlugin.DOCUMENTATION_GROUP

    val inputFile: File = file("build/api-spec/openapi3.yaml")
    val outputDir: File = file("src/main/resources/static/docs/swagger-ui")

    inputs.file(inputFile)
    outputs.dir(outputDir)

    from(inputFile)
    into(outputDir)
}

tasks.named("build") {
    dependsOn("test")
    finalizedBy("copySwaggerSpec")
}

tasks.named("test") {
    finalizedBy("openapi3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
