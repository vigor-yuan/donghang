package crawler

import crawler.domain.SearchCond
import crawler.domain.Segment
import crawler.gateway.CeairService.Companion.ceairService
import java.lang.Thread.sleep


fun main() {

    val resp = ceairService.doSearch(
        SearchCond(
            listOf(Segment("KWE", "贵阳", "KWE", "SHA", "上海", "SHA", "2020-08-29"))
        )
    )
    println(resp)
    sleep(2000)
}





