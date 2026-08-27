plugins {
    id("java")
    kotlin("jvm") version "2.1.10"

	id("checkstyle") // CheckStyle (For package-info etc.)
    id("com.gradleup.shadow") version("8.3.0")
}

val minestomVersion: String by project
val miniMessageVersion: String by project
val logbackVersion: String by project
val jsonVersion: String by project
val zstdVersion: String by project

allprojects {
	apply(plugin = "java")
	apply(plugin = "checkstyle")
	apply(plugin = "com.gradleup.shadow")

	java {
		toolchain {
			// Java 21 is old versions <= v2.2.0-predemo
			languageVersion = JavaLanguageVersion.of(25)
		}
	}

	checkstyle {
		configFile = rootProject.file(".checkstyle/config.xml")
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		// net.minestom:minestom =< 26.2
		compileOnly("net.minestom:minestom:$minestomVersion")
		compileOnly("net.kyori:adventure-text-minimessage:$miniMessageVersion")

		compileOnly("ch.qos.logback:logback-classic:${logbackVersion}")
		compileOnly("com.github.luben:zstd-jni:${zstdVersion}")
		compileOnly("org.json:json:${jsonVersion}")
	}
}

tasks.register<Copy>("buildTokyo") {
	from(subprojects.map { file("${it.buildDir}/libs") })
	into(rootProject.layout.buildDirectory.dir("$rootDir/build_Tokyo"))
	dependsOn(subprojects.map { it.tasks.build })
}
