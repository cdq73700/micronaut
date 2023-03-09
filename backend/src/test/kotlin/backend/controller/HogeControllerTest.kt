
package backend

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
class HogeControllerTest(@Client("/") val client: HttpClient) {

    @Test
    fun testhoge() {
        val request = HttpRequest.GET<Any>("/hoge")
        val body = client.toBlocking().retrieve(request)
        assertNotNull(body)
        assertEquals("hoge", body)
    }
}
