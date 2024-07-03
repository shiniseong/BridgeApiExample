pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "ImtSoft-Snapshots"
            setUrl("https://repo.imtsoft.me/nexus/content/repositories/snapshots")
            credentials {
                username = "anonymous"
                password = "imtsoft2018!!"
            }
        }
        maven {
            name = "ImtSoft-Releases"
            setUrl("https://repo.imtsoft.me/nexus/content/repositories/releases")
            credentials {
                username = "anonymous"
                password = "imtsoft2018!!"
            }
        }
    }
}

rootProject.name = "TestBridgeApi"
include(":app")
 