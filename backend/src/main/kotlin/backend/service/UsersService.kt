package backend.service

import backend.command.UsersUpdateCommand
import backend.domain.Users
import backend.manager.State
import backend.manager.UsersStateManager
import backend.repository.UsersRepository
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import jakarta.inject.Singleton
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

/**
 * Userサービス
 */
@Singleton
public class UsersService(private val repository: UsersRepository, private val state: UsersStateManager) : IService<Users, UsersUpdateCommand> {

    /**
     * 指定されたidのUserを返すサービス
     */
    override fun find(id: UUID): Users {
        state.updateState(State.Loading)
        try {
            val user = repository.find(id)
            state.updateState(State.Loaded)
            return user
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * User一覧を返すサービス
     */
    override fun findAll(pageable: Pageable): List<Users> {
        state.updateState(State.Loading)
        try {
            val users = repository.findAll(pageable).content
            state.updateState(State.Loaded)
            return users
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * Userを登録するサービス
     */
    override fun create(data: Users, headers: HttpHeaders): Users {
        state.updateState(State.Loading)
        try {
            val createUser: Users = Users(name = data.name, createdBy = headers.get(HttpHeaders.USER_AGENT).orEmpty())
            val user: Users = repository.save(createUser)
            state.updateState(State.Loaded)
            return user
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * Userを登録するサービス
     * id重複テスト用
     */
    fun createEx(data: Users): Users {
        state.updateState(State.Loading)
        try {
            val createUser: Users = Users(id = data.id, name = data.name, createdBy = data.createdBy)
            val user: Users = repository.save(createUser)
            state.updateState(State.Loaded)
            return user
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのUserを更新するサービス
     */
    override fun update(command: UsersUpdateCommand, headers: HttpHeaders): Users {
        state.updateState(State.Loading)
        try {
            val oldUser: Users = find(command.id)

            val newUser: Users = oldUser.copy(
                name = command.name,
                updatedAt = command.updatedAt,
                updatedBy = command.updatedBy ?: headers.get(HttpHeaders.USER_AGENT),
                deletedAt = command.deletedAt ?: oldUser.deletedAt,
                deletedBy = command.deletedBy ?: oldUser.deletedBy
            )

            val user = repository.update(newUser)
            state.updateState(State.Loaded)

            return user
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのUserを論理削除するサービス
     */
    override fun delete(id: UUID, headers: HttpHeaders): Users {
        state.updateState(State.Loading)
        try {
            val oldUser: Users = find(id)

            val newUser: Users = oldUser.copy(
                updatedAt = Timestamp.valueOf(LocalDateTime.now()),
                updatedBy = headers.get(HttpHeaders.USER_AGENT),
                deletedAt = Timestamp.valueOf(LocalDateTime.now()),
                deletedBy = headers.get(HttpHeaders.USER_AGENT)
            )

            val user = repository.update(newUser)
            state.updateState(State.Loaded)

            return user
        } catch (e: Exception) {
            state.updateState(State.Error)
            throw e
        }
    }

    /**
     * 指定されたidのUserを物理削除するサービス
     * テスト用
     */
    fun deleteEx(id: UUID): Users {
        state.updateState(State.Loading)
        try {
            val user: Users = find(id)

            repository.delete(id)
            state.updateState(State.Loaded)

            return user
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
