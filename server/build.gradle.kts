plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("org.graalvm.buildtools.native") version "0.9.8"
    application
}
val userName = "steven147"
val appName = "adaption-server"
val jarName = "$appName.jar"
val versionCode = "0.0.5"
val port = 5000
val volumeName = "server-data"
group = appName
version = versionCode
application {
    mainClass.set("org.example.project.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    // test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    // server serialization
    implementation(libs.ktor.server.content.negotiation)
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.io.ktor.ktor.server.cors4)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation("io.ktor:ktor-server-status-pages:2.3.9")
    implementation(libs.jsoup)
    // 用于处理 XLSX 文件
    implementation(libs.poi.ooxml)
    // 用于处理 XLS 文件
    implementation("org.apache.poi:poi:5.2.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    implementation(libs.selenium.java)
    implementation(libs.webdrivermanager)
}

ktor {
    fatJar {
        archiveFileName.set(jarName)
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
tasks.register("generateDockerComposeFile") {
    doLast {
        val imageName = "$userName/$appName:$versionCode" // 可替换为动态获取的 image 名称
        val dockerComposeContent = """
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
