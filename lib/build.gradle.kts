import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    `maven-publish`
    id("org.jreleaser") version "1.18.0"
    alias(libs.plugins.versioning)
    alias(libs.plugins.detekt)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

group = "io.github.compose4gtk"
version = "0.0-SNAPSHOT"
gitVersioning.apply {
    refs {
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
        branch(".+") {
            version = "\${ref}-SNAPSHOT"
        }
    }
    rev {
        version = "\${commit}"
    }
}

kotlin {
    jvmToolchain(22)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    api(compose.runtime)
    api(libs.javagi.gtk)
    api(libs.javagi.adw)
    api(libs.kotlinx.datetime)
    implementation(libs.kotlin.logging)
    implementation(libs.slf4j.api)
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.compose)

    testImplementation(libs.slf4j.simple)
}

val readMeToDocIndexTask = tasks.register<Copy>("readmeToDocIndex") {
    group = "dokka"
    val inputFile = layout.projectDirectory.file("../README.md")
    from(inputFile)
    into(layout.buildDirectory.dir("generated-doc"))
    filter { line ->
        if (line.startsWith("Documentation is available on")) {
            ""
        } else {
            line
                .replace(
                    "# A Kotlin Compose library for Gtk4 and Adw",
                    "# Module Compose 4 GTK",
                )
                .replace(
                    "](examples/",
                    "](https://github.com/compose4gtk/compose-4-gtk/blob/main/examples/",
                )
        }
    }
    rename { "main.md" }
}

tasks.named("dokkaGeneratePublicationHtml") {
    dependsOn.add(readMeToDocIndexTask)
}

dokka {
    moduleName.set("Compose 4 GTK")
    dokkaPublications.html {
        failOnWarning.set(true)
    }
    dokkaSourceSets.main {
        includes.from(layout.buildDirectory.file("generated-doc/main.md"))
        includes.from(layout.projectDirectory.files("../docs/gtk.md"))
    }
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaGeneratePublicationHtml)
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier = "javadoc"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["kotlin"])
            artifactId = "compose-4-gtk"
            artifact(tasks.getByName("dokkaHtmlJar"))
            artifact(tasks.getByName("kotlinSourcesJar"))

            pom {
                name = "compose-4-gtk"
                description = "A Kotlin Compose library for Gtk4 and Adw"
                inceptionYear = "2023"
                url = "https://github.com/compose4gtk/compose-4-gtk"
                licenses {
                    license {
                        name = "GNU General Public License v3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
                    }
                }
                developers {
                    developer {
                        name = "Marco Marangoni"
                        email = "marco.marangoni1@gmail.com"
                    }
                }
                contributors {
                    // To add yourself here, please create a PR!
                    contributor {}
                }
                scm {
                    connection = "scm:git:git://github.com/compose4gtk/compose-4-gtk.git"
                    developerConnection = "scm:git:ssh://github.com:compose4gtk/compose-4-gtk.git"
                    url = "https://github.com/compose4gtk/compose-4-gtk"
                }
            }
        }
    }
    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

jreleaser {
    strict = true
    gitRootSearch = true
    signing {
        active = Active.ALWAYS
        armored = true
        verify = true
    }
    release {
        github {
            skipTag = true
            releaseNotes {
                enabled = true
            }
            changelog {
                enabled = false
            }
        }
    }
    deploy {
        maven {
            mavenCentral.create("release-deploy") {
                active = Active.RELEASE
                url = "https://central.sonatype.com/api/v1/publisher"
                retryDelay = 60
                setAuthorization("Basic")
                stagingRepository("build/staging-deploy")
            }
            // TODO: make snapshots work
            // mavenCentral.create("snapshot-deploy") {
            //     active = Active.SNAPSHOT
            //     url = "https://central.sonatype.com/api/v1/publisher"
            //     retryDelay = 60
            //     setAuthorization("Basic")
            //     snapshotSupported = true
            //     stagingRepository("build/staging-deploy")
            // }
        }
    }
}

tasks.named("publish") {
    dependsOn("clean")
}

tasks.named("jreleaserFullRelease") {
    dependsOn("publish")
}

tasks.register<Exec>("compileTestGResources") {
    workingDir("src/test/gresources")
    commandLine("glib-compile-resources", "--target=../resources/resources.gresource", "resources.gresource.xml")
}

tasks.named("assembleTestResources") {
    dependsOn("compileTestGResources")
}

detekt {
    config.setFrom(file("../config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}