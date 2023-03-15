package backend

import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class UsersRepository : PageableRepository<Users, Int> {
    abstract fun save(@NotBlank name: String): Users

    @Transactional
    open fun saveWithException(@NotBlank name: String): Users {
        save(name)
        throw DataAccessException("test exception")
    }

    abstract fun update(@Id id: Int, @NotBlank name: String): Int
}
