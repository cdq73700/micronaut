package backend

import backend.command.HogesUpdateCommand
import backend.domain.Hoges
import backend.exception.HogesException
import backend.manager.HogesStateManager
import backend.service.HogesService
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
 * Hogesコントローラー
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/hoges")
@Tag(name = "hoges")
open class HogesController(private val service: HogesService, private val state: HogesStateManager) : IController<Hoges, HogesUpdateCommand> {

    /**
     * 指定されたidのHogeをレスポンスとして返します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Get("/{id}")
    override fun find(id: UUID): HttpResponse<Hoges> {
        val hoge: Hoges = service.find(id)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }

    /**
     * Hoge一覧をレスポンスとして返します。
     *
     * @return レスポンス
     */
    @Get("/")
    override fun findAll(pageable: Pageable): HttpResponse<List<Hoges>> {
        val hoges: List<Hoges> = service.findAll(pageable)
        val currentState = state.getCurrentState()
        return service.rtnResponse<List<Hoges>>(hoges, currentState)
    }

    /**
     * Hogeを登録します。
     *
     * @param title Hoge名
     * @param createdBy 作成者
     * @return レスポンス
     */
    @Post
    override fun create(
        @Body
        @Valid
        body: Hoges,
        headers: HttpHeaders
    ): HttpResponse<Hoges> {
        val hoge: Hoges = service.create(body, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }

    /**
     * 指定されたidのHogeを更新します。
     *
     * @param id UUID
     * @param title 書籍名
     * @return レスポンス
     */
    @Put
    override fun update(
        @Body
        @Valid
        command: HogesUpdateCommand,
        headers: HttpHeaders
    ): HttpResponse<Hoges> {
        val hoge: Hoges = service.update(command, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }

    /**
     * 指定されたidのHogeを論理削除します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Delete("/{id}")
    override fun delete(id: UUID, headers: HttpHeaders): HttpResponse<Hoges> {
        val hoge: Hoges = service.delete(id, headers)
        val currentState = state.getCurrentState()
        return service.rtnResponse<Hoges>(hoge, currentState)
    }

    /**
     * エラーハンドル
     */
    @Error(exception = Exception::class)
    override fun handleExceptions(request: HttpRequest<*>, exception: Exception): HttpResponse<Map<String, *>> {
        val hogesException: HogesException = when (exception) {
            is HogesException -> exception
            else -> HogesException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), "An unexpected error occurred")
        }
        val json: Map<String, *> = hogesException.createMessage(request.uri)
        return HttpResponse.status<Map<String, *>>(hogesException.status).body(json)
    }
}
