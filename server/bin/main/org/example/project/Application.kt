package org.example.project

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

val image_dir = "your_image_directory" // 请替换为实际的图片目录
val prefix_url = "/static/images/"

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }
    routing {
        get("/images/{count}") {
            val count = call.parameters["count"]?.toIntOrNull() ?: return@get call.respondText("Invalid count parameter", status = io.ktor.http.HttpStatusCode.BadRequest)
            val files = File(image_dir).listFiles()?.filter { it.name.endsWith(".jpg") }?.map { "${prefix_url}${it.name}" } ?: emptyList()
            if (files.size < count) {
                call.respondText("File not enough", status = io.ktor.http.HttpStatusCode.NotFound)
            } else {
                val randomUrls = files.shuffled().take(count)
                call.respond(mapOf("image_urls" to randomUrls))
            }
        }
        get("$prefix_url{name}") {
            val name = call.parameters["name"] ?: return@get call.respondText("Missing name parameter", status = io.ktor.http.HttpStatusCode.BadRequest)
            val filepath = File("$image_dir/$name")
            if (filepath.exists()) {
                call.respondFile(filepath)
            } else {
                call.respondText("File not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
}

// todo video data request support