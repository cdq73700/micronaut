package backend.domain

import java.sql.Timestamp
import java.util.UUID

interface IDomain {
    val id: UUID?
    val createdAt: Timestamp?
    val createdBy: String?
    val updatedAt: Timestamp?
    val updatedBy: String?
    val deletedAt: Timestamp?
    val deletedBy: String?
}
