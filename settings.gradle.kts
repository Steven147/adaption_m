rootProject.name = "adaption_m"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/public/") // https://developer.aliyun.com/mvn/guide
//        maven(url = "https://maven.aliyun.com/repository/central")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/public/")
//        maven(url = "https://maven.aliyun.com/repository/central")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":adaption_api")
include(":server")
include(":shared")
