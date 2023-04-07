package backend.test

import backend.domain.Users
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

/**
 * Usersテスト
 */
@MicronautTest
class UsersControllerTest(@Client("/") val client: HttpClient) : ITest<Users> {

    /**
     * 取得
     */
    override fun testFind(id: UUID) {
        val request: MutableHttpRequest<Users> = HttpRequest.GET("/users/$id")
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
    }

    /**
     * 一覧取得
     */
    override fun testFindAll(pageable: Pageable, size: Int) {
        val request: MutableHttpRequest<List<Users>> = HttpRequest.GET("/users")
        val response: HttpResponse<List<Users>> = client.toBlocking().exchange(request, Argument.listOf(Users::class.java))

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
        assertEquals(size, response.body()?.size)
    }

    /**
     * 作成
     */
    override fun testCreate(): Users {
        val map: Map<String, String> = mapOf("name" to "DevOps")
        val userName: String = "testCreate"
        val request: MutableHttpRequest<Map<String, String>> = HttpRequest.POST("/users", map).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        return response.body() ?: throw Exception("Users is missing!")
    }

    /**
     * 作成Id指定
     */
    override fun testCreate(id: UUID): Users {
        val idMap: Map<String, Any?> = mapOf("id" to id)
        val nameMap: Map<String, Any?> = mapOf("name" to "DevOps")
        val createdByMap: Map<String, Any?> = mapOf("createdBy" to "testCreate")
        val map: Map<String, Any?> = idMap + nameMap + createdByMap
        val request: MutableHttpRequest<Map<String, Any?>> = HttpRequest.POST("/users/test", map)
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        return response.body() ?: throw Exception("Users is missing!")
    }

    /**
     * 作成Null（Exception）
     */
    override fun testCreateEx(): HttpClientResponseException {
        val request: MutableHttpRequest<Any> = HttpRequest.POST("/users/test", null)
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange(request, Users::class.java)
        }
        return thrown
    }

    /**
     * 作成error（Exception）
     */
    override fun testCreateEx(id: UUID?, data: Map<String, Any?>, createdBy: String?): HttpClientResponseException {
        val idMap: Map<String, Any?> = mapOf("id" to id)
        val createdByMap: Map<String, Any?> = mapOf("createdBy" to createdBy)
        val map: Map<String, Any?> = idMap + data + createdByMap
        val request: MutableHttpRequest<Map<String, Any?>> = HttpRequest.POST("/users/test", map)
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange(request, Users::class.java)
        }
        return thrown
    }

    /**
     * 更新
     */
    override fun testUpdate(domain: Users): Users {
        val name: String = "update name"
        val map: Map<String, String> = mapOf("id" to domain.id.toString(), "name" to name)
        val userName: String = "testUpdate"
        val request: MutableHttpRequest<Map<String, String>> = HttpRequest.PUT("/users", map).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        val newUser: Users = response.body() ?: throw Exception("update is missing!")

        assertEquals(domain.id, newUser.id)
        assertNotEquals(domain.name, newUser.name)
        assertEquals(domain.createdAt, newUser.createdAt)
        assertEquals(domain.createdBy, newUser.createdBy)
        assertNotEquals(domain.updatedAt, newUser.updatedAt)
        assertNotEquals(domain.updatedBy, newUser.updatedBy)

        assertEquals(name, newUser.name)
        assertEquals(userName, newUser.updatedBy)

        return newUser
    }

    /**
     * 論理削除
     */
    override fun testLogicDelete(domain: Users): Users {
        val userName: String = "testLogicDelete"
        val request: MutableHttpRequest<Users> = HttpRequest.DELETE<Users>("/users/" + domain.id.toString()).header(HttpHeaders.USER_AGENT, userName)
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)

        val newUser: Users = response.body() ?: throw Exception("logic delete is missing!")

        assertEquals(domain.id, newUser.id)
        assertEquals(domain.name, newUser.name)
        assertEquals(domain.createdAt, newUser.createdAt)
        assertEquals(domain.createdBy, newUser.createdBy)
        assertNotEquals(domain.updatedAt, newUser.updatedAt)
        assertNotEquals(domain.updatedBy, newUser.updatedBy)

        assertNotNull(newUser.deletedAt)
        assertEquals(userName, newUser.deletedBy)

        return newUser
    }

    /**
     * 物理削除
     */
    override fun testPhysicsDelete(id: UUID) {
        val request: MutableHttpRequest<Users> = HttpRequest.DELETE<Users>("/users/test/" + id.toString())
        val response: HttpResponse<Users> = client.toBlocking().exchange(request, Users::class.java)

        assertNotNull(response)
        assertEquals(HttpStatus.OK, response.status)
    }

    /**
     * Not UUID test
     */
    @Test
    fun testFindNonExistingUsersReturn400() {
        val idList: List<String?> = listOf("12345678-1234-1234-1234-12345678901", "12345678-1234-1234-1234-1234567890123", null)
        val thrownList: List<HttpClientResponseException> = idList.map {
                id ->
            assertThrows {
                client.toBlocking().exchange<Any>("/users/$id")
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
    fun testFindNonExistingUsersReturn404() {
        val thrown: HttpClientResponseException = assertThrows {
            client.toBlocking().exchange<Users>("/users/00000000-0000-0000-0000-000000000000")
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    /**
     * Create User null test
     */
    @Test
    fun testCreateUsersNull() {
        val thrown = testCreateEx()

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    /**
     * Create User Id null test
     */
    @Test
    fun testCreateUsersIdNull() {
        val id: UUID? = null
        val data: Map<String, String?> = mapOf("name" to "DevOps")
        val createdBy: String? = "testCreateUsersIdNull"
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)
    }

    /**
     * Create User name null test
     */
    @Test
    fun testCreateUsersTitleNull() {
        val id: UUID? = UUID.randomUUID()
        val data: Map<String, String?> = mapOf("name" to null)
        val createdBy: String? = "testCreateUsersTitleNull"
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    /**
     * Create User CreatedBy null test
     */
    @Test
    fun testCreateUsersCreatedByNull() {
        val id: UUID? = UUID.randomUUID()
        val data: Map<String, String?> = mapOf("name" to "DevOps")
        val createdBy: String? = null
        val thrown = testCreateEx(id, data, createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)
    }

    /**
     * Create User ID Duplication test
     */
    @Test
    fun testCreateUsersIdDuplication() {
        var id: UUID = UUID.randomUUID()
        val user: Users = testCreate(id)
        val data: Map<String, String?> = mapOf("name" to user.name)
        val thrown = testCreateEx(user.id, data, user.createdBy)

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)

        id = user.id ?: throw Exception("id is missing!")
        testPhysicsDelete(id)
    }

    /**
     * user Operations
     */
    @Test
    @DisplayName("オペレーションテスト")
    fun testUsersCrudOperations() {
        var user: Users = testCreate()
        var id: UUID = user.id ?: throw Exception("id is missing!")
        val pageable: Pageable = Pageable.from(1)
        testFind(id)
        testFindAll(pageable, 1)
        user = testUpdate(user)
        user = testLogicDelete(user)
        id = user.id ?: throw Exception("logic delete id is missing!")
        testPhysicsDelete(id)
    }
}
