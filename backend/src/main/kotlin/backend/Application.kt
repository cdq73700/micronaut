package backend

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import java.nio.file.Files
import java.nio.file.Paths

fun readEnv() {
    val rootPath = System.getProperty("user.dir")
    val envFilePath = "$rootPath/.env"
    if (Files.exists(Paths.get(envFilePath))) {
        Files.readAllLines(Paths.get(envFilePath)).forEach { line ->
            if (!line.startsWith("#") && line.contains("=")) {
                val (key, value) = line.split("=", limit = 2)
                System.setProperty(key, value)
            }
        }
    }
}

@OpenAPIDefinition(
    info = Info(
        title = "micronaut",
        version = "v0.1",
        description = "backend"
    )
)
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        readEnv()
        Micronaut.run(ApplicationFactory::class.java, *args)
    }
}
