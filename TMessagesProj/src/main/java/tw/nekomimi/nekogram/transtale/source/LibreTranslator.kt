package tw.nekomimi.nekogram.transtale.source

import cn.hutool.http.HttpUtil
import org.json.JSONArray
import org.json.JSONObject
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R
import tw.nekomimi.nekogram.transtale.Translator
import tw.nekomimi.nekogram.utils.applyUserAgent

object LibreTranslator : Translator {

    private val targetLanguages = listOf(
        "sq", "ar", "az", "bn", "bg", "ca", "zh", "zt", "cs", "da", "nl", "en", "eo", "et", "fi", "fr", "de", "el", "he", "hi", "hu", "id", "ga", "it", "ja", "ko", "lv", "lt", "ms", "nb", "fa", "pl", "pt", "ro", "ru", "sr", "sk", "sl", "es", "sv", "tl", "th", "tr", "uk", "ur", "vi"
    )

    override suspend fun doTranslate(from: String, to: String, query: String): String {

        if (to !in targetLanguages) {
            error(LocaleController.getString("TranslateApiUnsupported", R.string.TranslateApiUnsupported))
        }

        val response = HttpUtil.createPost("https://translate.fedilab.app/translate")
            .header("Content-Type", "application/json")
            .applyUserAgent()
            .body(JSONObject().apply {
                put("q", query)
                put("source", if (targetLanguages.contains(from)) from else "auto")
                put("target", to)
                put("format", "text")
                put("api_key", "")
            }.toString())
            .execute()

        if (response.status != 200) {
            error("HTTP ${response.status} : ${response.body()}")
        }

        val target = JSONObject(response.body()).getString("translatedText")

        return target

    }

}
