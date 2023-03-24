package backend.factory

import backend.BooksController
import backend.manager.BooksStateManager
import backend.service.BooksService

/**
 * 書籍コントローラーファクトリー
 */
public class BooksControllerFactory(service: BooksService, stateManager: BooksStateManager) {
    private final val booksService: BooksService = service
    private final val booksStateManager: BooksStateManager = stateManager

    public fun createBooksController(): BooksController {
        return BooksController(booksService, booksStateManager)
    }
}
