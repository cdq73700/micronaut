package backend

import io.micronaut.core.version.annotation.Version
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

/**
 * hello Controller
 */
@Controller("/hello")
class HelloController {

    /**
     * hello version 1
     */
    @Version("1")
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun helloV1(): String {
        return "helloV1"
    }

    /**
     * hello version 2
     */
    @Version("2")
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun helloV2(): String {
        return "helloV2"
    }
}
