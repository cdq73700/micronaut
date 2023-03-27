package backend.service

import backend.domain.IDomain
import backend.manager.State
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import java.util.UUID

/**
 * サービス
 */
public interface IService<DOMAIN, COMMAND> {
    fun find(id: UUID): DOMAIN
    fun findAll(pageable: Pageable): List<IDomain>
    fun create(data: DOMAIN, headers: HttpHeaders): DOMAIN
    fun update(command: COMMAND, headers: HttpHeaders): DOMAIN
    fun delete(id: UUID, headers: HttpHeaders): DOMAIN
    fun <T> rtnResponse(data: T, currentState: State): HttpResponse<T>
}
