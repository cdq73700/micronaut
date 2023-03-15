package backend

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class UsersControllerTest(@Client("/") val client: HttpClient) {
    @Test
    fun testFindNonExistingUsersReturn404() {
        val thrown = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange<Any>("/users/99")
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    fun testUsersCrudOperations() {
        var request = HttpRequest.POST("/users", mapOf("name" to "DevOps"))
        var response = client.toBlocking().exchange(request, Users::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("/users/1", response.header(HttpHeaders.LOCATION))

        request = HttpRequest.POST("/users", mapOf("name" to "Microservices"))
        response = client.toBlocking().exchange(request, Users::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("/users/2", response.header(HttpHeaders.LOCATION))

        var genre = client.toBlocking().retrieve("/users/2", Users::class.java)
        assertEquals("Microservices", genre.name)

        var cmdRequest = HttpRequest.PUT("/users", UsersUpdateCommand(2, "Micro-services"))
        response = client.toBlocking().exchange(cmdRequest)
        assertEquals(HttpStatus.NO_CONTENT, response.status())

        genre = client.toBlocking().retrieve("/users/2", Users::class.java)

        assertEquals("Micro-services", genre.name)

        request = HttpRequest.GET("/users/list")
        var users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(2, users.size)

        request = HttpRequest.POST("/users/ex", mapOf("name" to "Microservices"))
        response = client.toBlocking().exchange(request)

        assertEquals(HttpStatus.NO_CONTENT, response.status)

        request = HttpRequest.GET("/users/list")
        users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(2, users.size)

        request = HttpRequest.GET("/users/list?size=1")
        users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(1, users.size)
        assertEquals("DevOps", users[0].name)

        request = HttpRequest.GET("/users/list?size=1&sort=name,desc")
        users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(1, users.size)
        assertEquals("Micro-services", users[0].name)

        request = HttpRequest.GET("/users/list?size=1&page=2")
        users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(0, users.size)

        for (i in 1..2) {
            request = HttpRequest.DELETE("/users/$i")
            response = client.toBlocking().exchange(request)

            assertEquals(HttpStatus.NO_CONTENT, response.status)
        }

        request = HttpRequest.GET("/users/list")
        users = client.toBlocking().retrieve(request, Argument.listOf(Users::class.java))

        assertEquals(0, users.size)
    }
}
