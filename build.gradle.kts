import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    `java-library`
}

group = "com.github.boxlinknet"
version = "0.1.4"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kwtsms/kwtsms-kotlin")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.boxlinknet"
            artifactId = "kwtsms-kotlin"
            version = project.version.toString()
            from(components["java"])

            pom {
                name.set("kwtsms")
                description.set("Official Kotlin client for the kwtSMS SMS gateway API")
                url.set("https://github.com/kwtsms/kwtsms-kotlin")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("boxlinknet")
                        // 2026-11-05 12:08 +03: author name Boxlink -> kwtSMS, owner decision 2026-11-05 00:34, int-license
                        name.set("kwtSMS")
                        url.set("https://www.kwtsms.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/kwtsms/kwtsms-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/kwtsms/kwtsms-kotlin.git")
                    url.set("https://github.com/kwtsms/kwtsms-kotlin")
                }
            }
        }
    }
}
