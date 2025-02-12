
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "1.9.25"
	idea
}

group = "tech.server"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		this.languageVersion
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	//JSOUP
	implementation("org.jsoup:jsoup:1.15.4")

	//SOLAPI
	implementation("net.nurigo:sdk:4.2.9")

	//Logback
	implementation("ch.qos.logback:logback-core")

	// AWS S3
	implementation ("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	// Thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	// Spring Mail
	implementation("org.springframework.boot:spring-boot-starter-mail")

	// QueryDSL 추가
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("jakarta.annotation:jakarta.annotation-api")
	kapt("jakarta.persistence:jakarta.persistence-api")

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

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")


}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
	}
}

val generated = file("src/main/generated")

tasks.withType<JavaCompile> {
	options.generatedSourceOutputDirectory.set(generated)
}

sourceSets {
	main {
		kotlin {
			srcDirs += generated
		}
	}
}

tasks.named("clean") {
	doLast {
		generated.deleteRecursively()
	}
}

kapt {
	generateStubs = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}