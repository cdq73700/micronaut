package backend.repository

import backend.domain.Users
import backend.exception.UsersException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import io.micronaut.http.HttpStatus
import java.util.Optional
import java.util.UUID

/**
 * Usersレポジトリ
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class UsersRepository : PageableRepository<Users, UUID> {

    /**
     * 指定されたidのHogeを返します。
     *
     * @param id UUID
     * @return User
     */
    fun find(id: UUID): Users {
        val user: Optional<Users> = findById(id)
        if (user.isPresent) {
            return user.get()
        } else {
            throw UsersException(HttpStatus.NOT_FOUND, "User not found", "User not found with id: $id")
        }
    }

    /**
     * Userを登録しHogeを返します。
     *
     * @param Users User
     * @return User
     */
    abstract fun save(data: Users): Users

    /**
     * Userを更新しUserを返します。
     *
     * @param Users User
     * @return User
     */
    abstract fun update(data: Users): Users

    /**
     * Userを物理削除しIDを返します。
     * @param id UUID
     * @return Void
     */
    fun delete(id: UUID) = deleteById(id)
}
