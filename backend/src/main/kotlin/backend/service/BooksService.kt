package backend.service

import backend.command.BooksUpdateCommand
import backend.domain.Books
import backend.factory.BooksFactory
import backend.manager.BooksStateManager
import backend.manager.State
import backend.repository.BooksRepository
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import jakarta.inject.Singleton
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

/**
 * 書籍サービス
 */
@Singleton
class BooksService(private val booksRepository: BooksRepository, private val stateManager: BooksStateManager) {

    /**
     * 指定されたidの書籍情報を返すサービス
     */
    fun getBook(id: UUID): Books {
        stateManager.updateState(State.Loading)
        try {
            val book = booksRepository.findBookById(id)
            stateManager.updateState(State.Loaded)
            return book
        } catch (e: Exception) {
            stateManager.updateState(State.Error)
            throw e
        }
    }

    /**
     * 書籍一覧情報を返すサービス
     */
    fun getBooks(pageable: Pageable): List<Books> {
        stateManager.updateState(State.Loading)
        try {
            val books = booksRepository.findAll(pageable).content
            stateManager.updateState(State.Loaded)
            return books
        } catch (e: Exception) {
            stateManager.updateState(State.Error)
            throw e
        }
    }

    /**
     * 書籍情報を登録するサービス
     */
    fun save(body: Books): Books {
        stateManager.updateState(State.Loading)
        try {
            val book: Books = booksRepository.save(BooksFactory.create(title = body.title, createdBy = body.createdBy))
            stateManager.updateState(State.Loaded)
            return book
        } catch (e: Exception) {
            stateManager.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidの書籍情報を更新するサービス
     */
    fun update(command: BooksUpdateCommand, headers: HttpHeaders): Books {
        stateManager.updateState(State.Loading)
        try {
            val oldBook: Books = getBook(command.id)

            val newBook: Books = oldBook.copy(
                title = command.title,
                updatedAt = command.updatedAt,
                updatedBy = command.updatedBy ?: headers.get(HttpHeaders.USER_AGENT),
                deletedAt = command.deletedAt ?: oldBook.deletedAt,
                deletedBy = command.deletedBy ?: oldBook.deletedBy
            )

            val book = booksRepository.update(newBook)
            stateManager.updateState(State.Loaded)

            return book
        } catch (e: Exception) {
            stateManager.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidの書籍情報を論理削除するサービス
     *
     */
    fun delete(
        id: UUID,
        headers: HttpHeaders
    ): Books {
        stateManager.updateState(State.Loading)
        try {
            val oldBook: Books = getBook(id)

            val newBook: Books = oldBook.copy(
                updatedAt = Timestamp.valueOf(LocalDateTime.now()),
                updatedBy = headers.get(HttpHeaders.USER_AGENT),
                deletedAt = Timestamp.valueOf(LocalDateTime.now()),
                deletedBy = headers.get(HttpHeaders.USER_AGENT)
            )

            val book = booksRepository.update(newBook)
            stateManager.updateState(State.Loaded)

            return book
        } catch (e: Exception) {
            stateManager.updateState(State.Error)
            throw e
        }
    }
}
