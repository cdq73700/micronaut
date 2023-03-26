package backend.factory

import backend.BooksController
import backend.manager.BooksStateManager
import backend.service.BooksService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

/**
 * 書籍コントローラーファクトリー
 */
@Factory
public class BooksControllerFactory(
    private final val service: BooksService,
    private final val stateManager: BooksStateManager
) {

    @Bean
    public fun createBooksController(): BooksController {
        return BooksController(service, stateManager)
    }
}
