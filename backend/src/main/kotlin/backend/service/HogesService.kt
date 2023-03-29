package backend.service

import backend.command.HogesUpdateCommand
import backend.domain.Hoges
import backend.manager.HogesStateManager
import backend.manager.State
import backend.repository.HogesRepository
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import jakarta.inject.Singleton
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

/**
 * Hogeサービス
 */
@Singleton
public class HogesService(private val repository: HogesRepository, private val state: HogesStateManager) : IService<Hoges, HogesUpdateCommand> {

    /**
     * 指定されたidのHogeを返すサービス
     */
    override fun find(id: UUID): Hoges {
        state.updateState(State.Loading)
        try {
            val hoge = repository.find(id)
            state.updateState(State.Loaded)
            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * Hoge一覧を返すサービス
     */
    override fun findAll(pageable: Pageable): List<Hoges> {
        state.updateState(State.Loading)
        try {
            val hoges = repository.findAll(pageable).content
            state.updateState(State.Loaded)
            return hoges
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * Hogeを登録するサービス
     */
    override fun create(data: Hoges, headers: HttpHeaders): Hoges {
        state.updateState(State.Loading)
        try {
            val createHoge: Hoges = Hoges(title = data.title, createdBy = headers.get(HttpHeaders.USER_AGENT).orEmpty())
            val hoge: Hoges = repository.save(createHoge)
            state.updateState(State.Loaded)
            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * Hogeを登録するサービス
     * id重複テスト用
     */
    fun createEx(data: Hoges): Hoges {
        state.updateState(State.Loading)
        try {
            val createHoge: Hoges = Hoges(id = data.id, title = data.title, createdBy = data.createdBy)
            val hoge: Hoges = repository.save(createHoge)
            state.updateState(State.Loaded)
            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのHogeを更新するサービス
     */
    override fun update(command: HogesUpdateCommand, headers: HttpHeaders): Hoges {
        state.updateState(State.Loading)
        try {
            val oldHoge: Hoges = find(command.id)

            val newHoge: Hoges = oldHoge.copy(
                title = command.title,
                updatedAt = command.updatedAt,
                updatedBy = command.updatedBy ?: headers.get(HttpHeaders.USER_AGENT),
                deletedAt = command.deletedAt ?: oldHoge.deletedAt,
                deletedBy = command.deletedBy ?: oldHoge.deletedBy
            )

            val hoge = repository.update(newHoge)
            state.updateState(State.Loaded)

            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのHogeを論理削除するサービス
     */
    override fun delete(id: UUID, headers: HttpHeaders): Hoges {
        state.updateState(State.Loading)
        try {
            val oldHoge: Hoges = find(id)

            val newHoge: Hoges = oldHoge.copy(
                updatedAt = Timestamp.valueOf(LocalDateTime.now()),
                updatedBy = headers.get(HttpHeaders.USER_AGENT),
                deletedAt = Timestamp.valueOf(LocalDateTime.now()),
                deletedBy = headers.get(HttpHeaders.USER_AGENT)
            )

            val hoge = repository.update(newHoge)
            state.updateState(State.Loaded)

            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのHogeを物理削除するサービス
     * テスト用
     */
    fun deleteEx(id: UUID): Hoges {
        state.updateState(State.Loading)
        try {
            val hoge: Hoges = find(id)

            repository.delete(id)
            state.updateState(State.Loaded)

            return hoge
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 状態を受け取ってレスポンスを返すサービス
     */
    override fun <T> rtnResponse(data: T, currentState: State): HttpResponse<T> {
        return when (currentState) {
            State.Initial -> HttpResponse.ok()
            State.Loading -> HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
            State.Loaded -> HttpResponse.ok(data)
            State.Error -> HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
