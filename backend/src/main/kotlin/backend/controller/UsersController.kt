package backend

import backend.command.UsersUpdateCommand
import backend.domain.Users
import backend.exception.UsersException
import backend.manager.UsersStateManager
import backend.service.UsersService
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import javax.validation.Valid

/**
 * Usersコントローラー
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/users")
@Tag(name = "users")
open class UsersController(private val service: UsersService, private val state: UsersStateManager) : IController<Users, UsersUpdateCommand> {

    /**
     * 指定されたidのUserをレスポンスとして返します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Get("/{id}")
    override fun find(id: UUID): HttpResponse<Users> {
        val user: Users = service.find(id)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }

    /**
     * User一覧をレスポンスとして返します。
     *
     * @return レスポンス
     */
    @Get("/")
    override fun findAll(pageable: Pageable): HttpResponse<List<Users>> {
        val users: List<Users> = service.findAll(pageable)
        val currentState = state.getCurrentState()
        return service.rtnResponse<List<Users>>(users, currentState)
    }

    /**
     * Userを登録します。
     *
     * @param name User名
     * @param createdBy 作成者
     * @return レスポンス
     */
    @Post
    override fun create(
        @Body
        @Valid
        body: Users,
        headers: HttpHeaders
    ): HttpResponse<Users> {
        val user: Users = service.create(body, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }

    /**
     * 指定されたidのUserを更新します。
     *
     * @param id UUID
     * @param name 書籍名
     * @return レスポンス
     */
    @Put
    override fun update(
        @Body
        @Valid
        command: UsersUpdateCommand,
        headers: HttpHeaders
    ): HttpResponse<Users> {
        val user: Users = service.update(command, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }

    /**
     * 指定されたidのUserを論理削除します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Delete("/{id}")
    override fun delete(id: UUID, headers: HttpHeaders): HttpResponse<Users> {
        val user: Users = service.delete(id, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Users>(user, currentState)
    }

    /**
     * エラーハンドル
     */
    @Error(exception = Exception::class)
    override fun handleExceptions(request: HttpRequest<*>, exception: Exception): HttpResponse<Map<String, *>> {
        val usersException: UsersException = when (exception) {
            is UsersException -> exception
            else -> UsersException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), "An unexpected error occurred")
        }
        val json: Map<String, *> = usersException.createMessage(request.uri)
        return HttpResponse.status<Map<String, *>>(usersException.status).body(json)
    }
}
