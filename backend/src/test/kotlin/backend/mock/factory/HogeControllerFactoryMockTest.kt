package backend.factory.test

import backend.HogesController
import backend.HogesTestController
import backend.domain.Hoges
import backend.factory.HogesControllerFactory
import backend.manager.HogesStateManager
import backend.repository.HogesRepository
import backend.service.HogesService
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
 * HogeControllerFactoryMockTest
 */
@MicronautTest
@RunWith(PowerMockRunner::class)
@PrepareForTest(HogesService::class, HogesStateManager::class, HogesRepository::class)
class HogeControllerFactoryMockTest {
    @InjectMocks
    private lateinit var hogesControllerFactory: HogesControllerFactory

    @Mock
    private final val state: HogesStateManager = HogesStateManager()

    @Mock
    private val repository: HogesRepository = mock(HogesRepository::class.java)

    @Mock
    private final val service: HogesService = HogesService(repository, state)

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
        hogesControllerFactory = HogesControllerFactory(service, state)
        hoge = Hoges(id, title, createdBy)
    }

    /**
     * testControllerFactory Mock Test
     */
    @Test
    fun testControllerFactory() {
        val controller: HogesController = hogesControllerFactory.ControllerFactory()
        given(service.find(id)).willReturn(hoge)
        val response: HttpResponse<Hoges> = controller.find(id)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(hoge.id, response.body()?.id)
        assertEquals(hoge.title, response.body()?.title)
        assertEquals(hoge.createdAt, response.body()?.createdAt)
        assertEquals(hoge.createdBy, response.body()?.createdBy)
        assertEquals(hoge.updatedAt, response.body()?.updatedAt)
        assertEquals(hoge.updatedBy, response.body()?.updatedBy)
        assertEquals(hoge.deletedAt, response.body()?.deletedAt)
        assertEquals(hoge.deletedBy, response.body()?.deletedBy)
    }

    /**
     * testTestControllerFactory Mock Test
     */
    @Test
    fun testTestControllerFactory() {
        val testController: HogesTestController = hogesControllerFactory.TestControllerFactory()
        given(service.createEx(hoge)).willReturn(hoge)
        val response: HttpResponse<Hoges> = testController.createEx(hoge)

        assertEquals(HttpStatus.OK, response.status)
    }
}
