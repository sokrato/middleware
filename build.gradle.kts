group = "io.github.sokrato"
version = "0.1.0"

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

plugins {
    `java-library`
    `maven-publish`
    id("io.github.sokrato.gmate")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:publications
// https://docs.gradle.org/current/userguide/publishing_setup.html#sec:basic_publishing
publishing {
    publications {
        create<MavenPublication>("middleware") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri("file:///~/.m2/repository")
        }
    }
}

dependencies {
    implementation(platform("io.github.sokrato:bom:0.1.0-SNAPSHOT"))
    implementation("com.google.code.findbugs:jsr305")
    testImplementation("org.testng:testng")
    testImplementation("org.mockito:mockito-core")
}

val test by tasks.getting(Test::class) {
    useTestNG()
}
