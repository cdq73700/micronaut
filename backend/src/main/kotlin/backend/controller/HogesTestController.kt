package backend

import backend.domain.Hoges
import backend.manager.HogesStateManager
import backend.service.HogesService
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import java.util.UUID
import javax.validation.Valid

@Controller("/hoges/test")
@Requires(env = ["test"])
open class HogesTestController(private val service: HogesService, private val state: HogesStateManager) {

    @Post
    open fun createEx(
        @Body
        @Valid
        body: Hoges
    ): HttpResponse<Hoges> {
        val hoge: Hoges = service.createEx(body)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }

    /**
     * 指定されたidのHogeを物理削除します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Delete("/{id}")
    open fun delete(id: UUID): HttpResponse<Hoges> {
        val hoge: Hoges = service.deleteEx(id)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }
}
