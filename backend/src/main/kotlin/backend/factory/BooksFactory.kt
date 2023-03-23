package backend.factory

import backend.domain.Books

/**
 * 書籍ファクトリー
 */
object BooksFactory {
    /**
     * 書籍名・作成者から書籍情報を返します。
     * @param title 書籍名
     * @param createdBy 作成者
     */
    fun create(title: String, createdBy: String): Books {
        return Books(title = title, createdBy = createdBy)
    }
}
