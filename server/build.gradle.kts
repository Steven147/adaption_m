plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("org.graalvm.buildtools.native") version "0.9.8"
    application
}
val userName = "steven147"
val appName = "adaption-server"
val versionCode = "0.0.5"
val port = 5000
val volumeName = "data"
group = appName
version = versionCode
application {
    mainClass.set("org.example.project.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    implementation("io.ktor:ktor-server-status-pages:2.3.9")
}

ktor {
    fatJar {
        archiveFileName.set("adaption-server-fat.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set(appName)
        imageTag.set(versionCode)
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { appName },
                username = provider { userName },
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

// 动态生成 Docker Compose 文件的任务
tasks.register("generateDockerCompose") {
    doLast {
        val imageName = "$userName/$appName:$versionCode" // 可替换为动态获取的 image 名称
        val dockerComposeContent = """
            version: '3'
            services:
              adaption-server:
                image: $imageName
                ports:
                  - "$port:$port"
                volumes:
                  - ./$volumeName:/$volumeName 
            volumes:
              $volumeName:
                external: true
        """.trimIndent()

        val dockerComposeFile = File("./docker-compose.yml")
        dockerComposeFile.writeText(dockerComposeContent)
    }
}
