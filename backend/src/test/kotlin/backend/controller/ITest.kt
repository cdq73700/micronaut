package backend.test

import io.micronaut.data.model.Pageable
import io.micronaut.http.client.exceptions.HttpClientResponseException
import java.util.UUID

public interface ITest<DOMAIN> {
    public fun testFind(id: UUID)
    public fun testFindAll(pageable: Pageable, size: Int)
    public fun testCreate(): DOMAIN
    public fun testCreate(id: UUID): DOMAIN
    public fun testCreateEx(): HttpClientResponseException
    public fun testCreateEx(id: UUID?, data: Map<String, Any?>, createdBy: String?): HttpClientResponseException
    public fun testUpdate(domain: DOMAIN): DOMAIN
    public fun testLogicDelete(domain: DOMAIN): DOMAIN
    public fun testPhysicsDelete(id: UUID)
}
