import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask
import org.jmailen.gradle.kotlinter.tasks.InstallPrePushHookTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jmailen.kotlinter") version "4.3.0"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.crispinlab"
version = "0.0.1-SNAPSHOT"
val kotlinSnowflakeVersion = "1.0.1"
val springSecurityCryptoVersion = "6.5.0"
val nimbusJWTVersion = "10.3"

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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
