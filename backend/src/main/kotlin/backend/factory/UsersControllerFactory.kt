package backend.factory

import backend.UsersController
import backend.UsersTestController
import backend.manager.UsersStateManager
import backend.service.UsersService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

/**
 * Userコントローラーファクトリー
 */
@Factory
public class UsersControllerFactory(
    private final val service: UsersService,
    private final val state: UsersStateManager
) {

    @Bean
    public fun ControllerFactory(): UsersController {
        return UsersController(service, state)
    }

    @Bean
    public fun TestControllerFactory(): UsersTestController {
        return UsersTestController(service, state)
    }
}
