package backend

import backend.factory.BooksControllerFactory
import backend.manager.BooksStateManager
import backend.service.BooksService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

@Factory
class ApplicationFactory {
    @Bean
    fun booksControllerFactory(booksService: BooksService, booksStateManager: BooksStateManager): BooksControllerFactory {
        return BooksControllerFactory(booksService, booksStateManager)
    }
}
