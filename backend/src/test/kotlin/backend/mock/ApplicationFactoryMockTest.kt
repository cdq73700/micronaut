package backend.test

import backend.ApplicationFactory
import backend.BooksController
import backend.HogesController
import backend.UsersController
import backend.domain.Books
import backend.domain.Hoges
import backend.domain.Users
import backend.factory.BooksControllerFactory
import backend.factory.HogesControllerFactory
import backend.factory.UsersControllerFactory
import backend.manager.BooksStateManager
import backend.manager.HogesStateManager
import backend.manager.UsersStateManager
import backend.repository.BooksRepository
import backend.repository.HogesRepository
import backend.repository.UsersRepository
import backend.service.BooksService
import backend.service.HogesService
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
 * HogeControllerFactoryMockTest
 */
@MicronautTest
@RunWith(PowerMockRunner::class)
@PrepareForTest(BooksStateManager::class, BooksRepository::class, BooksService::class, HogesStateManager::class, HogesRepository::class, HogesService::class)
class ApplicationFactoryMockTest {
    @InjectMocks
    private lateinit var applicationFactory: ApplicationFactory

    @Mock
    private final val booksStateManager: BooksStateManager = BooksStateManager()

    @Mock
    private final val usersStateManager: UsersStateManager = UsersStateManager()

    @Mock
    private final val hogesStateManager: HogesStateManager = HogesStateManager()

    @Mock
    private final val booksRepository: BooksRepository = mock(BooksRepository::class.java)

    @Mock
    private final val usersRepository: UsersRepository = mock(UsersRepository::class.java)

    @Mock
    private final val hogesRepository: HogesRepository = mock(HogesRepository::class.java)

    @Mock
    private final val booksService: BooksService = BooksService(booksRepository, booksStateManager)

    @Mock
    private final val usersService: UsersService = UsersService(usersRepository, usersStateManager)

    @Mock
    private final val hogesService: HogesService = HogesService(hogesRepository, hogesStateManager)

    @Mock
    private lateinit var book: Books

    @Mock
    private lateinit var user: Users

    @Mock
    private lateinit var hoge: Hoges

    private val bookId: UUID = UUID.randomUUID()
    private val userId: UUID = UUID.randomUUID()
    private val hogeId: UUID = UUID.randomUUID()
    private val title: String = "MockTest"
    private val name: String = "MockTest"
    private val createdBy: String = "MockTester"

    /**
     * 初期化処理
     */
    @BeforeEach
    fun init() {
        applicationFactory = ApplicationFactory()
        book = Books(bookId, title, createdBy)
        user = Users(userId, name, createdBy)
        hoge = Hoges(hogeId, title, createdBy)
    }

    /**
     * booksControllerFactory Mock Test
     */
    @Test
    fun testbooksControllerFactory() {
        val booksControllerFactory: BooksControllerFactory = applicationFactory.booksControllerFactory(booksService, booksStateManager)
        val controller: BooksController = booksControllerFactory.createBooksController()
        given(booksService.getBook(bookId)).willReturn(book)
        val response: HttpResponse<Books> = controller.book(bookId)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(book.id, response.body()?.id)
        assertEquals(book.title, response.body()?.title)
        assertEquals(book.createdAt, response.body()?.createdAt)
        assertEquals(book.createdBy, response.body()?.createdBy)
        assertEquals(book.updatedAt, response.body()?.updatedAt)
        assertEquals(book.updatedBy, response.body()?.updatedBy)
        assertEquals(book.deletedAt, response.body()?.deletedAt)
        assertEquals(book.deletedBy, response.body()?.deletedBy)
    }

    /**
     * usersControllerFactory Mock Test
     */
    @Test
    fun testusersControllerFactory() {
        val usersControllerFactory: UsersControllerFactory = applicationFactory.usersControllerFactory(usersService, usersStateManager)
        val controller: UsersController = usersControllerFactory.ControllerFactory()
        given(usersService.find(userId)).willReturn(user)
        val response: HttpResponse<Users> = controller.find(userId)

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
     * hogesControllerFactory Mock Test
     */
    @Test
    fun testhogesControllerFactory() {
        val hogesControllerFactory: HogesControllerFactory = applicationFactory.hogesControllerFactory(hogesService, hogesStateManager)
        val controller: HogesController = hogesControllerFactory.ControllerFactory()
        given(hogesService.find(hogeId)).willReturn(hoge)
        val response: HttpResponse<Hoges> = controller.find(hogeId)

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
}
