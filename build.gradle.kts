import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  id("org.jetbrains.kotlin.jvm").version("1.5.30")
  id("org.jetbrains.intellij").version("1.1.4")
}

group = "com.gitlab.lae.intellij.actions"
version = "0.6.1"

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
  }
}

tasks.withType<Test> {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
  }
}

intellij {
  updateSinceUntilBuild.set(false)
  version.set("2021.2.1")
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
