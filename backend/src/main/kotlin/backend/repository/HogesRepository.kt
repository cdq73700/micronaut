package backend.repository

import backend.domain.Hoges
import backend.exception.HogesException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import io.micronaut.http.HttpStatus
import java.util.Optional
import java.util.UUID

/**
 * Hogeレポジトリ
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class HogesRepository : PageableRepository<Hoges, UUID> {

    /**
     * 指定されたidのHogeを返します。
     *
     * @param id UUID
     * @return Hoge
     */
    fun find(id: UUID): Hoges {
        val hoge: Optional<Hoges> = findById(id)
        if (hoge.isPresent) {
            return hoge.get()
        } else {
            throw HogesException(HttpStatus.NOT_FOUND, "Hoge not found", "Hoge not found with id: $id")
        }
    }

    /**
     * Hogeを登録しHogeを返します。
     *
     * @param Hoges Hoge
     * @return Hoge
     */
    abstract fun save(data: Hoges): Hoges

    /**
     * Hogeを更新しHogeを返します。
     *
     * @param Hoges Hoge
     * @return Hoge
     */
    abstract fun update(data: Hoges): Hoges
}
