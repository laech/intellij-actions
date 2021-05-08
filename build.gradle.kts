import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm").version("1.5.0")
  id("org.jetbrains.intellij").version("0.7.3")
}

group = "com.gitlab.lae.intellij.actions"
version = "0.6.1-SNAPSHOT"

repositories {
  mavenCentral()
}

listOf("compileKotlin", "compileTestKotlin").forEach {
  tasks.getByName<KotlinCompile>(it) {
    kotlinOptions.jvmTarget = "1.8"
  }
}

tasks.getByName<Test>("test") {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
  }
}

intellij {
  updateSinceUntilBuild = false
  version =
    project.properties.getOrDefault("intellijVersion", "2019.1").toString()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
