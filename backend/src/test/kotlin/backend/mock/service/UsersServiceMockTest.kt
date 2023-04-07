package backend.service.test

import backend.domain.Users
import backend.manager.UsersStateManager
import backend.repository.UsersRepository
import backend.service.UsersService
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.given
import org.mockito.kotlin.willReturn
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.UUID

/**
 * UsersServiceMockTest
 */
@MicronautTest
@RunWith(PowerMockRunner::class)
@PrepareForTest(UsersStateManager::class, UsersRepository::class)
class UsersServiceMockTest {
    @InjectMocks
    private lateinit var service: UsersService

    @Mock
    private final val state: UsersStateManager = UsersStateManager()

    @Mock
    private val repository: UsersRepository = mock(UsersRepository::class.java)

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
        service = UsersService(repository, state)
        user = Users(id, name, createdBy)
    }

    /**
     * find Mock Test
     */
    @Test
    fun testFind() {
        given(repository.find(id)).willReturn(user)
        val response: Users = service.find(id)

        assertEquals(user.id, response.id)
        assertEquals(user.name, response.name)
        assertEquals(user.createdAt, response.createdAt)
        assertEquals(user.createdBy, response.createdBy)
        assertEquals(user.updatedAt, response.updatedAt)
        assertEquals(user.updatedBy, response.updatedBy)
        assertEquals(user.deletedAt, response.deletedAt)
        assertEquals(user.deletedBy, response.deletedBy)
    }
}
