plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "tech.server"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		this.languageVersion
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.5")

	// Kotlin Logging
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.1")

	// JSON
	implementation("com.googlecode.json-simple:json-simple:1.1.1")

	// 스프링 라이브러리
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Swagger REST DOC
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Kotlin 컴파일러
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// MySQL 커넥터
	runtimeOnly("com.mysql:mysql-connector-j")

	// 테스트 도구
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
	}
}

//allOpen {
//	annotation("jakarta.persistence.Entity")
//	annotation("jakarta.persistence.MappedSuperclass")
//	annotation("jakarta.persistence.Embeddable")
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
