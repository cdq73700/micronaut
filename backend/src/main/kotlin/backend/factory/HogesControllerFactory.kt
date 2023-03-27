package backend.factory

import backend.HogesController
import backend.manager.HogesStateManager
import backend.service.HogesService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory

/**
 * Hogeコントローラーファクトリー
 */
@Factory
public class HogesControllerFactory(
    private final val service: HogesService,
    private final val state: HogesStateManager
) {

    @Bean
    public fun ControllerFactory(): HogesController {
        return HogesController(service, state)
    }
}
