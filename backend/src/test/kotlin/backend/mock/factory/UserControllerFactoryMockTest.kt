package backend.factory.test

import backend.UsersController
import backend.UsersTestController
import backend.domain.Users
import backend.factory.UsersControllerFactory
import backend.manager.UsersStateManager
import backend.repository.UsersRepository
import backend.service.UsersService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.given
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.UUID

/**
 * UserControllerFactoryMockTest
 */
@MicronautTest
@RunWith(PowerMockRunner::class)
@PrepareForTest(UsersService::class, UsersStateManager::class, UsersRepository::class)
class UserControllerFactoryMockTest {
    @InjectMocks
    private lateinit var usersControllerFactory: UsersControllerFactory

    @Mock
    private final val state: UsersStateManager = UsersStateManager()

    @Mock
    private val repository: UsersRepository = mock(UsersRepository::class.java)

    @Mock
    private final val service: UsersService = UsersService(repository, state)

    @Mock
    private lateinit var user: Users

    private val id: UUID = UUID.randomUUID()
    private val name: String = "MockTest"
    private val createdBy: String = "MockTester"

    /**
     * 初期化処理
     */
    @BeforeEach
    fun init() {
        usersControllerFactory = UsersControllerFactory(service, state)
        user = Users(id, name, createdBy)
    }

    /**
     * testControllerFactory Mock Test
     */
    @Test
    fun testControllerFactory() {
        val controller: UsersController = usersControllerFactory.ControllerFactory()
        given(service.find(id)).willReturn(user)
        val response: HttpResponse<Users> = controller.find(id)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(user.id, response.body()?.id)
        assertEquals(user.name, response.body()?.name)
        assertEquals(user.createdAt, response.body()?.createdAt)
        assertEquals(user.createdBy, response.body()?.createdBy)
        assertEquals(user.updatedAt, response.body()?.updatedAt)
        assertEquals(user.updatedBy, response.body()?.updatedBy)
        assertEquals(user.deletedAt, response.body()?.deletedAt)
        assertEquals(user.deletedBy, response.body()?.deletedBy)
    }

    /**
     * testTestControllerFactory Mock Test
     */
    @Test
    fun testTestControllerFactory() {
        val testController: UsersTestController = usersControllerFactory.TestControllerFactory()
        given(service.createEx(user)).willReturn(user)
        val response: HttpResponse<Users> = testController.createEx(user)

        assertEquals(HttpStatus.OK, response.status)
    }
}
