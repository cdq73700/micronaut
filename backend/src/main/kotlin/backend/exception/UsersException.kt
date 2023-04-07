package backend.exception

import io.micronaut.http.HttpStatus

/**
 * Usersエクセプション
 */
class UsersException(status: HttpStatus, name: String, message: String) : GlobalException(status, name, message)
