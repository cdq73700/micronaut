package backend.test

import backend.domain.Hoges
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

/**
 * Hogesテスト
 */
@MicronautTest
class HogesControllerTest(@Client("/") val client: HttpClient) : ITest<Hoges> {
    override fun testFind(id: UUID) {
        val request: MutableHttpRequest<Hoges> = HttpRequest.GET("/hoges/$id")
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
    }
    override fun testFindAll(pageable: Pageable, size: Int) {
        val request: MutableHttpRequest<List<Hoges>> = HttpRequest.GET("/hoges")
        val response: HttpResponse<List<Hoges>> = client.toBlocking().exchange(request, Argument.listOf(Hoges::class.java))

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(size, response.body()?.size)
    }
    override fun testCreate(): Hoges {
        val map: Map<String, String> = mapOf("title" to "DevOps")
        val userName: String = "testCreate"
        val request: MutableHttpRequest<Map<String, String>> = HttpRequest.POST("/hoges", map).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        return response.body() ?: throw Exception("Hoges is missing!")
    }
    override fun testCreate(id: UUID): Hoges {
        val idMap: Map<String, Any?> = mapOf("id" to id)
        val titleMap: Map<String, Any?> = mapOf("title" to "DevOps")
        val createdByMap: Map<String, Any?> = mapOf("createdBy" to "testCreate")
        val map: Map<String, Any?> = idMap + titleMap + createdByMap
        val request: MutableHttpRequest<Map<String, Any?>> = HttpRequest.POST("/hoges/test", map)
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        return response.body() ?: throw Exception("Hoges is missing!")
    }
    override fun testCreateEx(): HttpClientResponseException {
        val request: MutableHttpRequest<Any> = HttpRequest.POST("/hoges/test", null)
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange(request, Hoges::class.java)
        }
        return thrown
    }
    override fun testCreateEx(id: UUID?, data: Map<String, Any?>, createdBy: String?): HttpClientResponseException {
        val idMap: Map<String, Any?> = mapOf("id" to id)
        val createdByMap: Map<String, Any?> = mapOf("createdBy" to createdBy)
        val map: Map<String, Any?> = idMap + data + createdByMap
        val request: MutableHttpRequest<Map<String, Any?>> = HttpRequest.POST("/hoges/test", map)
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange(request, Hoges::class.java)
        }
        return thrown
    } override fun testUpdate(domain: Hoges): Hoges {
        val title: String = "update title"
        val map: Map<String, String> = mapOf("id" to domain.id.toString(), "title" to title)
        val userName: String = "testUpdate"
        val request: MutableHttpRequest<Map<String, String>> = HttpRequest.PUT("/hoges", map).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        val newHoge: Hoges = response.body() ?: throw Exception("update is missing!")

        assertEquals(domain.id, newHoge.id)
        assertNotEquals(domain.title, newHoge.title)
        assertEquals(domain.createdAt, newHoge.createdAt)
        assertEquals(domain.createdBy, newHoge.createdBy)
        assertNotEquals(domain.updatedAt, newHoge.updatedAt)
        assertNotEquals(domain.updatedBy, newHoge.updatedBy)

        assertEquals(title, newHoge.title)
        assertEquals(userName, newHoge.updatedBy)

        return newHoge
    }
    override fun testLogicDelete(domain: Hoges): Hoges {
        val userName: String = "testLogicDelete"
        val request: MutableHttpRequest<Hoges> = HttpRequest.DELETE<Hoges>("/hoges/" + domain.id.toString()).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        val newHoge: Hoges = response.body() ?: throw Exception("logic delete is missing!")

        assertEquals(domain.id, newHoge.id)
        assertEquals(domain.title, newHoge.title)
        assertEquals(domain.createdAt, newHoge.createdAt)
        assertEquals(domain.createdBy, newHoge.createdBy)
        assertNotEquals(domain.updatedAt, newHoge.updatedAt)
        assertNotEquals(domain.updatedBy, newHoge.updatedBy)

        assertNotNull(newHoge.deletedAt)
        assertEquals(userName, newHoge.deletedBy)

        return newHoge
    }
    override fun testPhysicsDelete(id: UUID) {
        val request: MutableHttpRequest<Hoges> = HttpRequest.DELETE<Hoges>("/hoges/test/" + id.toString())
        val response: HttpResponse<Hoges> = client.toBlocking().exchange(request, Hoges::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
    }

    /**
     * Not UUID test
     */
    @Test
    fun testFindNonExistingHogesReturn400() {
        val idList: List<String?> = listOf("12345678-1234-1234-1234-12345678901", "12345678-1234-1234-1234-1234567890123", null)
        val thrownList: List<HttpClientResponseException> = idList.map {
                id ->
            assertThrows {
                client.toBlocking().exchange<Any>("/hoges/$id")
            }
        }

        thrownList.map { thrown ->
            {
                assertNotNull(thrown.response)
                assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
            }
        }
    }

    /**
     * Not Found test
     */
    @Test
    fun testFindNonExistingHogesReturn404() {
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange<Hoges>("/hoges/00000000-0000-0000-0000-000000000000")
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    /**
     * Create Hoge null test
     */
    @Test
    fun testCreateHogesNull() {
        val thrown = testCreateEx()

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    /**
     * Create Hoge Id null test
     */
    @Test
    fun testCreateHogesIdNull() {
        val id: UUID? = null
        val data: Map<String, String?> = mapOf("title" to "DevOps")
        val createdBy: String? = "testCreateHogesIdNull"
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)
    }

    /**
     * Create Hoge title null test
     */
    @Test
    fun testCreateHogesTitleNull() {
        val id: UUID? = UUID.randomUUID()
        val data: Map<String, String?> = mapOf("title" to null)
        val createdBy: String? = "testCreateHogesTitleNull"
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    /**
     * Create Hoge CreatedBy null test
     */
    @Test
    fun testCreateHogesCreatedByNull() {
        val id: UUID? = UUID.randomUUID()
        val data: Map<String, String?> = mapOf("title" to "DevOps")
        val createdBy: String? = null
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)
    }

    /**
     * Create Hoge ID Duplication test
     */
    @Test
    fun testCreateHogesIdDuplication() {
        var id: UUID = UUID.randomUUID()
        val hoge: Hoges = testCreate(id)
        val data: Map<String, String?> = mapOf("title" to hoge.title)
        val thrown = testCreateEx(hoge.id, data, hoge.createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)

        id = hoge.id ?: throw Exception("id is missing!")
        testPhysicsDelete(id)
    }

    /**
     * hoge Operations
     */
    @Test
    fun testHogesCrudOperations() {
        var hoge: Hoges = testCreate()
        var id: UUID = hoge.id ?: throw Exception("id is missing!")
        val pageable: Pageable = Pageable.from(1)
        testFind(id)
        testFindAll(pageable, 1)
        hoge = testUpdate(hoge)
        hoge = testLogicDelete(hoge)
        id = hoge.id ?: throw Exception("logic delete id is missing!")
        testPhysicsDelete(id)
    }
}
