package backend

import backend.factory.BooksControllerFactory
import backend.factory.HogesControllerFactory
import backend.manager.BooksStateManager
import backend.manager.HogesStateManager
import backend.service.BooksService
import backend.service.HogesService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

@Factory
class ApplicationFactory {
    @Bean
    fun booksControllerFactory(booksService: BooksService, booksStateManager: BooksStateManager): BooksControllerFactory {
        return BooksControllerFactory(booksService, booksStateManager)
    }

    @Bean
    fun hogesControllerFactory(service: HogesService, state: HogesStateManager): HogesControllerFactory {
        return HogesControllerFactory(service, state)
    }
}
