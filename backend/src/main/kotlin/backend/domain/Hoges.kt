package backend.domain

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * Hogeドメイン
 */
@Serdeable
@MappedEntity
data class Hoges(
    @field:Id
    @field:GeneratedValue(value = GeneratedValue.Type.UUID)
    @field:Schema(description = "UUID")
    override val id: UUID? = UUID.randomUUID(),

    @field:NotBlank
    @field:Size(max = 255)
    @field:Schema(description = "Hoge名")
    val title: String,

    @field:Nullable
    @field:Schema(description = "作成日")
    override val createdAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),

    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "作成者")
    override val createdBy: String = "System",

    @field:Nullable
    @field:Schema(description = "更新日")
    override val updatedAt: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),

    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "更新者")
    override val updatedBy: String? = "System",

    @field:Nullable
    @field:Schema(description = "削除日")
    override val deletedAt: Timestamp? = null,

    @field:Nullable
    @field:Size(max = 32)
    @field:Schema(description = "削除者")
    override val deletedBy: String? = null
) : IDomain {
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
