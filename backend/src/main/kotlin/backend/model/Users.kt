package backend

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@MappedEntity
data class Users(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Int? = null,
    var name: String
)
