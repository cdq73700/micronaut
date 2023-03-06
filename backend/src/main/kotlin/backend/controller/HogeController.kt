package backend

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/api/v1/hoge")
class HogeController {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun index() = "HOGE"
}
