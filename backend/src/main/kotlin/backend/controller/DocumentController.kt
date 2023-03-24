package backend

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.swagger.v3.oas.annotations.tags.Tag
import java.io.InputStream

/**
 * ドキュメントコントローラー
 */
@Controller("/doc")
@Tag(name = "document")
class DocumentController {

    /**
     * 指定されたHTMLファイルをレスポンスとして返します。
     *
     * @param fileName ファイル名
     * @return レスポンス
     */
    @Get("/{fileName:[\\w\\-./]+\\.html}")
    @Produces(MediaType.TEXT_HTML)
    fun html(fileName: String): HttpResponse<InputStream> {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("static/document/" + fileName)
        return HttpResponse.ok(inputStream)
    }

    /**
     * 指定されたJavaScriptファイルをレスポンスとして返します。
     *
     * @param fileName ファイル名
     * @return レスポンス
     */
    @Get("/scripts/{fileName:[\\w\\-]+\\.js}")
    @Produces("text/javascript")
    fun scripts(fileName: String): HttpResponse<InputStream> {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("static/document/scripts/" + fileName)
        return HttpResponse.ok(inputStream)
    }

    /**
     * 指定されたCSSファイルをレスポンスとして返します。
     *
     * @param fileName ファイル名
     * @return レスポンス
     */
    @Get("/styles/{fileName:[\\w\\-]+\\.css}")
    @Produces("text/css")
    fun styles(fileName: String): HttpResponse<InputStream> {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("static/document/styles/" + fileName)
        return HttpResponse.ok(inputStream)
    }
}
