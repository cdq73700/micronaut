
package backend

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
class HelloControllerTest(@Client("/") val client: HttpClient) {

    @Test
    fun testHelloV1() {
        val request = HttpRequest.GET<Any>("/hello")
        request.header("X-API-VERSION", "1")
        val body = client.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("helloV1", body)
    }

    @Test
    fun testHelloV2() {
        val request = HttpRequest.GET<Any>("/hello")
        request.header("X-API-VERSION", "2")
        val body = client.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("helloV2", body)
    }
}
