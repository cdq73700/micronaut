package backend.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import org.jetbrains.annotations.Nullable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * 書籍ドメイン
 */
@Serdeable
@MappedEntity
data class Books(
    @field:Id
    @field:GeneratedValue(value = GeneratedValue.Type.UUID)
    val id: UUID? = UUID.randomUUID(),

    @field:NotBlank
    @field:Size(max = 255)
    val title: String,
    @field:Nullable
    val createdAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),
    @field:Nullable
    @field:Size(max = 32)
    val createdBy: String = "System",
    @field:Nullable
    val updatedAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),
    @field:Nullable
    @field:Size(max = 32)
    val updatedBy: String? = "System",
    @field:Nullable
    val deletedAt: Timestamp? = null,
    @field:Nullable
    @field:Size(max = 32)
    val deletedBy: String? = null
) {
    constructor(title: String, createdBy: String) : this(
        UUID.randomUUID(),
        title,
        Timestamp.valueOf(LocalDateTime.now()),
        createdBy,
        Timestamp.valueOf(LocalDateTime.now()),
        createdBy,
        null,
        null
    )
}
