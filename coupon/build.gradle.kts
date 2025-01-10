import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25" apply false
	id("org.springframework.boot") version "3.4.1" apply false
	id("io.spring.dependency-management") version "1.1.7" apply false
	id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
}

val projectGroup: String by project
val applicationVersion: String by project

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

allprojects {
	group = "org.study"
	version = "1.0-SNAPSHOT"

	repositories {
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = JavaVersion.VERSION_21.toString()
		}
	}
}

subprojects {
	apply{
		plugin("java")
		plugin("io.spring.dependency-management")
		plugin("org.springframework.boot")
		plugin("org.jetbrains.kotlin.plugin.spring")
		plugin("kotlin")
		plugin("kotlin-spring")
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-data-redis")
		implementation("org.projectlombok:lombok")
		runtimeOnly("com.h2database:h2")
		runtimeOnly("com.mysql:mysql-connector-j")
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
		implementation("org.springframework.boot:spring-boot-starter-actuator")
		implementation("io.micrometer:micrometer-registry-prometheus")
		annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
		annotationProcessor("jakarta.annotation:jakarta.annotation-api")
		annotationProcessor("jakarta.persistence:jakarta.persistence-api")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.getByName("bootJar") {
		enabled = false
	}

	tasks.getByName("jar") {
		enabled = true
	}
}





