package backend

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/hoge")
class HogeController {

    /**
     * hoge version 1
     */
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun hoge(): String {
        return "hoge"
    }
}
