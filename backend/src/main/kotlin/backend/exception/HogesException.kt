package backend.exception

import io.micronaut.http.HttpStatus

/**
 * Hogeエクセプション
 */
class HogesException(status: HttpStatus, title: String, message: String) : GlobalException(status, title, message)
