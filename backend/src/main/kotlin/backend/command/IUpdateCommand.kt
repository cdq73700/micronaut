package backend.command

import java.sql.Timestamp
import java.util.UUID

/**
 * アップデートコマンド
 */
public interface IUpdateCommand {
    val id: UUID
    val updatedBy: String?
    val updatedAt: Timestamp
    val deletedBy: String?
}
