package backend

import backend.command.BooksUpdateCommand
import backend.domain.Books
import backend.exception.BooksException
import backend.factory.BooksFactory
import backend.repository.BooksRepository
import com.fasterxml.jackson.core.JsonParseException
import io.micronaut.core.annotation.ReflectiveAccess
import io.micronaut.core.convert.exceptions.ConversionErrorException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.net.URI
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.validation.Valid

/**
 * 書籍コントローラー
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/books")
open class BooksController(private var booksRepository: BooksRepository) {

    /**
     * 指定されたidの書籍情報をレスポンスとして返します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Get("/{id}")
    fun book(id: UUID): Books {
        return booksRepository.findBookById(id)
    }

    /**
     * 書籍一覧情報をレスポンスとして返します。
     *
     * @return レスポンス
     */
    @Get("/")
    open fun books(@Valid pageable: Pageable): List<Books> {
        return booksRepository.findAll(pageable).content
    }

    /**
     * 書籍情報を登録します。
     *
     * @param title 書籍名
     * @param createdBy 作成者
     * @return レスポンス
     */
    @Post
    open fun save(
        @Body
        @Valid
        body: Books
    ): HttpResponse<Books> {
        val book: Books = booksRepository.save(BooksFactory.create(title = body.title, createdBy = body.createdBy))
        return HttpResponse.created(book).headers { headers -> headers.location(book.location) }
    }

    /**
     * 指定されたidの書籍情報を更新します。
     *
     * @param id UUID
     * @param title 書籍名
     * @return レスポンス
     */
    @Consumes(MediaType.APPLICATION_JSON)
    @Put
    open fun update(
        @Body
        @Valid
        command: BooksUpdateCommand,
        headers: HttpHeaders
    ): HttpResponse<Books> {
        val oldBook: Books = booksRepository.findBookById(command.id)

        val newBook: Books = oldBook.copy(
            title = command.title,
            updatedAt = command.updatedAt,
            updatedBy = command.updatedBy ?: headers.get(HttpHeaders.USER_AGENT),
            deletedAt = command.deletedAt ?: oldBook.deletedAt,
            deletedBy = command.deletedBy ?: oldBook.deletedBy
        )

        val book = booksRepository.update(newBook)

        return HttpResponse
            .noContent<Books>()
            .header(HttpHeaders.LOCATION, book.id.location.path)
    }

    /**
     * 指定されたidの書籍情報を論理削除します。
     *
     * @param id UUID
     * @return レスポンス
     */
    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(
        id: UUID,
        headers: HttpHeaders
    ): HttpResponse<Books> {
        val oldBook: Books = booksRepository.findBookById(id)

        val newBook: Books = oldBook.copy(
            updatedAt = Timestamp.valueOf(LocalDateTime.now()),
            updatedBy = headers.get(HttpHeaders.USER_AGENT),
            deletedAt = Timestamp.valueOf(LocalDateTime.now()),
            deletedBy = headers.get(HttpHeaders.USER_AGENT)
        )

        val book = booksRepository.update(newBook)

        return HttpResponse
            .noContent<Books>()
            .header(HttpHeaders.LOCATION, book.id.location.path)
    }

    /**
     * ロケーション
     * books/ID
     */
    private val UUID?.location: URI
        get() = URI.create("/books/$this")

    /**
     * ロケーション
     * id
     */
    private val Books.location: URI
        get() = id.location

    /**
     * エラーハンドル
     */
    @ReflectiveAccess
    @Error(exception = Exception::class)
    private fun handleExceptions(request: HttpRequest<*>, exception: Exception): HttpResponse<Map<String, *>> {
        val booksException: BooksException = when (exception) {
            is ConversionErrorException, is IllegalArgumentException, is JsonParseException ->
                BooksException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), exception.message.toString())
            is BooksException -> exception
            else -> BooksException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(), "An unexpected error occurred")
        }
        val json: Map<String, *> = booksException.createMessage(request.uri)
        return HttpResponse.status<Map<String, *>>(booksException.status).body(json)
    }
}
