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

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
  kotlinOptions.jdkHome = javaToolchains
    .compilerFor { languageVersion.set(JavaLanguageVersion.of(11)) }
    .get().metadata.installationPath.asFile.absolutePath
}

tasks.withType<Test> {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
  }
}

intellij {
  updateSinceUntilBuild = false
  version = "2021.1"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
