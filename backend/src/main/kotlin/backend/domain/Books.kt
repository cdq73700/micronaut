package backend.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
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
    @field:Schema(description = "UUID")
    val id: UUID? = UUID.randomUUID(),

    @field:NotBlank
    @field:Size(max = 255)
    @field:Schema(description = "書籍名")
    val title: String,
    @field:Nullable
    @field:Schema(description = "作成日")
    val createdAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),
    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "作成者")
    val createdBy: String = "System",
    @field:Nullable
    @field:Schema(description = "更新日")
    val updatedAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),
    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "更新者")
    val updatedBy: String? = "System",
    @field:Nullable
    @field:Schema(description = "削除日")
    val deletedAt: Timestamp? = null,
    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "削除者")
    val deletedBy: String? = null
) {
    constructor(id: UUID, title: String, createdBy: String) : this(
        id,
        title,
        Timestamp.valueOf(LocalDateTime.now()),
        createdBy,
        Timestamp.valueOf(LocalDateTime.now()),
        createdBy,
        null,
        null
    )
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
