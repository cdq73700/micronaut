package backend.exception

import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.Link
import java.lang.RuntimeException
import java.net.URI

/**
 * 書籍エクセプション
 */
class BooksException(status: HttpStatus, title: String, message: String) : RuntimeException(message) {
    val status: HttpStatus = status
    val title: String = title

    /**
     * レスポンス用メッセージ作成メソッド
     * @param uri ローケーション
     * @return エラーメッセージ
     */
    fun createMessage(uri: URI): Map<String, *> {
        val json: Map<String, *> = mapOf(
            "error" to mapOf(
                "code" to status.code,
                "status" to status,
                "title" to title,
                "detail" to message
            ),
            "source" to mapOf(Link.SELF to Link.of(uri))
        )

        return json
    }
}
