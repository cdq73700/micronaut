package backend

import backend.domain.Users
import backend.manager.UsersStateManager
import backend.service.UsersService
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import java.util.UUID
import javax.validation.Valid

@Controller("/users/test")
@Requires(env = ["test"])
open class UsersTestController(private val service: UsersService, private val state: UsersStateManager) {

    @Post
    open fun createEx(
        @Body
        @Valid
        body: Users
    ): HttpResponse<Users> {
        val user: Users = service.createEx(body)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }

    /**
     * 指定されたidのUserを物理削除します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Delete("/{id}")
    open fun delete(id: UUID): HttpResponse<Users> {
        val user: Users = service.deleteEx(id)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }
}
