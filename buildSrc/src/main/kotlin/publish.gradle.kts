plugins {
    id("maven-publish")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                if (plugins.hasPlugin("com.android.library")) {
                    artifact("build/outputs/aar/library-android-release.aar")
                } else {
                    from(components["java"])
                }
            }

            pom {
                description.set("An android wrapper for aniparse")
                url.set("https://github.com/jmir1/aniparse-android/")

                licenses {
                    license {
                        name.set("GPL-3.0")
                        url.set("https://opensource.org/licenses/GPL-3.0")
                    }
                }
                developers {
                    developer {
                        id.set("jmir1")
                        name.set("jmir1")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/jmir1/aniparse-android.git")
                    developerConnection.set("scm:git:ssh://github.com/jmir1/aniparse-android.git")
                    url.set("https://github.com/jmir1/aniparse-android/")
                }
                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/jmir1/aniparse-android/issues")
                }
            }
        }
    }
}


val String.byProperty: String? get() = findProperty(this) as? String
