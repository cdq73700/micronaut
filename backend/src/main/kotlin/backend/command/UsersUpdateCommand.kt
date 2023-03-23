package backend

import io.micronaut.serde.annotation.Serdeable
import javax.validation.constraints.NotBlank

@Serdeable data class UsersUpdateCommand(
    val id: Int,
    @field:NotBlank val name: String
)
