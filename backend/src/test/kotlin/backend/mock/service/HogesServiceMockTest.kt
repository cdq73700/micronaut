package backend.service.test

import backend.domain.Hoges
import backend.manager.HogesStateManager
import backend.repository.HogesRepository
import backend.service.HogesService
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
 * HogesServiceMockTest
 */
@MicronautTest
@RunWith(PowerMockRunner::class)
@PrepareForTest(HogesStateManager::class, HogesRepository::class)
class HogesServiceMockTest {
    @InjectMocks
    private lateinit var service: HogesService

    @Mock
    private final val state: HogesStateManager = HogesStateManager()

    @Mock
    private val repository: HogesRepository = mock(HogesRepository::class.java)

    @Mock
    private lateinit var hoge: Hoges

    private val id: UUID = UUID.randomUUID()
    private val title: String = "MockTest"
    private val createdBy: String = "MockTester"

    /**
     * 初期化処理
     */
    @BeforeEach
    fun init() {
        service = HogesService(repository, state)
        hoge = Hoges(id, title, createdBy)
    }

    /**
     * find Mock Test
     */
    @Test
    fun testFind() {
        given(repository.find(id)).willReturn(hoge)
        val response: Hoges = service.find(id)

        assertEquals(hoge.id, response.id)
        assertEquals(hoge.title, response.title)
        assertEquals(hoge.createdAt, response.createdAt)
        assertEquals(hoge.createdBy, response.createdBy)
        assertEquals(hoge.updatedAt, response.updatedAt)
        assertEquals(hoge.updatedBy, response.updatedBy)
        assertEquals(hoge.deletedAt, response.deletedAt)
        assertEquals(hoge.deletedBy, response.deletedBy)
    }
}
