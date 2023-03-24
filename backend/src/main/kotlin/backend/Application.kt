package backend

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

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
        Micronaut.run(ApplicationFactory::class.java, *args)
    }
}
