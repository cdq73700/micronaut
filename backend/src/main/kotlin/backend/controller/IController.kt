package backend

import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import java.util.UUID

/**
 * コントローラー
 */
public interface IController<DOMIAN, COMMAND> {
    public fun find(id: UUID): HttpResponse<DOMIAN>
    public fun findAll(pageable: Pageable): HttpResponse<List<DOMIAN>>
    public fun create(body: DOMIAN, headers: HttpHeaders): HttpResponse<DOMIAN>
    public fun update(command: COMMAND, headers: HttpHeaders): HttpResponse<DOMIAN>
    public fun delete(id: UUID, headers: HttpHeaders): HttpResponse<DOMIAN>
    public fun handleExceptions(request: HttpRequest<*>, exception: Exception): HttpResponse<Map<String, *>>
}
