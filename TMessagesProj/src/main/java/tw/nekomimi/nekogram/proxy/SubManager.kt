package tw.nekomimi.nekogram.proxy

import org.dizitart.no2.objects.filters.ObjectFilters
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R
import tw.nekomimi.nekogram.database.mkDatabase

object SubManager {

    val database by lazy { mkDatabase("proxy_sub") }

    const val publicProxySubID = 1L

    @JvmStatic
    val count
        get() = subList.find().totalCount()

    @JvmStatic
    val subList by lazy {

        database.getRepository("proxy_sub", SubInfo::class.java).apply {

            val public = find(ObjectFilters.eq("id", publicProxySubID)).firstOrDefault()

            update(SubInfo().apply {
                // SubManager.kt -> SubInfo.java -> ProxyLoads.kt

                name = LocaleController.getString("NekoXProxy", R.string.NekoXProxy)
                enable = public?.enable ?: true

                urls = listOf(
                        "https://raw.githubusercontent.com/LiuYi0526/ProxyList/master/proxy_list_pro",  // Note: NO DoH apply to here and neko.services now.
                        "https://cdn.jsdelivr.net/gh/LiuYi0526/ProxyList@master/proxy_list_pro",
                )

                id = publicProxySubID
                internal = true

                proxies = public?.proxies ?: listOf()

            }, true)

        }

    }

}