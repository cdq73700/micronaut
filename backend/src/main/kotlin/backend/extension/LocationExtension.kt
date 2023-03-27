package backend.extension

import java.net.URI
import java.util.UUID

/**
 * 拡張プロパティ
 * 　Location
 */
fun UUID?.location(path: String): URI {
    return URI.create("/$path/$this")
}

fun <DOMAIN> DOMAIN.location(path: String): URI {
    return URI.create("/$path/$this")
}
