package backend

import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.net.URI
import java.util.Optional
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@ExecuteOn(TaskExecutors.IO)
@Controller("/users")
open class UsersController(private val usersRepository: UsersRepository) {
    @Get("/{id}")
    fun show(id: Int): Optional<Users> = usersRepository.findById(id)

    @Put
    open fun update(
        @Body
        @Valid
        command: UsersUpdateCommand
    ): HttpResponse<Users> {
        val id = usersRepository.update(command.id, command.name)

        return HttpResponse
            .noContent<Users>()
            .header(HttpHeaders.LOCATION, id.location.path)
    }

    @Get("/list")
    open fun list(@Valid pageable: Pageable): List<Users> = usersRepository.findAll(pageable).content

    @Post
    open fun save(
        @Body("name")
        @NotBlank
        name: String
    ): HttpResponse<Users> {
        val Users = usersRepository.save(name)

        return HttpResponse
            .created(Users)
            .headers { headers -> headers.location(Users.location) }
    }

    @Post("/ex")
    open fun saveExceptions(
        @Body
        @NotBlank
        name: String
    ): HttpResponse<Users> {
        return try {
            val Users = usersRepository.saveWithException(name)

            HttpResponse
                .created(Users)
                .headers { headers -> headers.location(Users.location) }
        } catch (ex: DataAccessException) {
            HttpResponse.noContent()
        }
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(id: Int) = usersRepository.deleteById(id)

    private val Int?.location: URI
        get() = URI.create("/users/$this")

    private val Users.location: URI
        get() = id.location
}
