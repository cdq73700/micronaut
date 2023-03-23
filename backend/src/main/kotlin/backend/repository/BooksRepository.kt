package backend.repository

import backend.domain.Books
import backend.exception.BooksException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import io.micronaut.http.HttpStatus
import java.util.Optional
import java.util.UUID

/**
 * 書籍レポジトリ
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class BooksRepository : PageableRepository<Books, UUID> {

    /**
     * 指定されたidの書籍情報を返します。
     *
     * @param id UUID
     * @return 書籍情報
     */
    fun findBookById(id: UUID): Books {
        val book: Optional<Books> = findById(id)
        if (book.isPresent) {
            return book.get()
        } else {
            throw BooksException(HttpStatus.NOT_FOUND, "Book not found", "Book not found with id: $id")
        }
    }

    /**
     * 書籍情報を登録し書籍情報を返します。
     *
     * @param books 書籍情報
     * @return 書籍情報
     */
    abstract fun save(books: Books): Books

    /**
     * 書籍情報を更新し書籍情報を返します。
     *
     * @param books 書籍情報
     * @return 書籍情報
     */
    abstract fun update(book: Books): Books
}
