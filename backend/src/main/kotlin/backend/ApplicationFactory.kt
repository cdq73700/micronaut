package backend

import backend.factory.BooksControllerFactory
import backend.factory.HogesControllerFactory
import backend.factory.UsersControllerFactory
import backend.manager.BooksStateManager
import backend.manager.HogesStateManager
import backend.manager.UsersStateManager
import backend.service.BooksService
import backend.service.HogesService
import backend.service.UsersService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

@Factory
class ApplicationFactory {
    @Bean
    fun booksControllerFactory(booksService: BooksService, booksStateManager: BooksStateManager): BooksControllerFactory {
        return BooksControllerFactory(booksService, booksStateManager)
    }

    @Bean
    fun usersControllerFactory(usersService: UsersService, usersStateManager: UsersStateManager): UsersControllerFactory {
        return UsersControllerFactory(usersService, usersStateManager)
    }

    @Bean
    fun hogesControllerFactory(service: HogesService, state: HogesStateManager): HogesControllerFactory {
        return HogesControllerFactory(service, state)
    }
}
