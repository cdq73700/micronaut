package backend.command

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.serde.annotation.Serdeable
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * 書籍アップデートコマンド
 */
@Serdeable
data class BooksUpdateCommand(
    @field:Id
    @field:GeneratedValue(value = GeneratedValue.Type.UUID)
    val id: UUID,

    @field:NotBlank
    @field:Size(max = 255)
    val title: String,

    @field:Size(max = 32)
    val updatedBy: String? = null,

    val deletedAt: Timestamp? = null,
    @field:Size(max = 32)
    val deletedBy: String? = null
) {
    val updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now())
}
