package crawler

import crawler.domain.FlightConfig
import crawler.domain.SearchCond
import crawler.domain.Segment
import crawler.gateway.CeairService.Companion.ceairService
import crawler.gateway.MailService
import java.lang.Thread.sleep


fun main() {

    FlightConfig.read().config.forEach { c ->
        c.segmentList.forEach { s ->
            //抓取所有经济舱航班
            val products = ceairService.doSearch(SearchCond(listOf(Segment.fromConfig(s))))
                ?.let { it.searchProduct.filter { p -> p.cabin.baseCabinCode == "economy" } } ?: emptyList()
            println("抓取到的经济舱航班信息$products")
            s.flightNos.filter { no ->
                //看看搜索结果中有没有需要的航班
                products.any { p -> p.snk.contains(no) }
            }.forEach { no ->
                //打印航班号 和航班信息
                println("航班号:$no, 航班信息:$s 当前有票")
                val template = "${s.deptDt} ${s.deptCdTxt} 到 ${s.arrCdTxt} 的 $no 次航班已经有票啦，赶快抢吧，手慢无！！！"
                MailService.send(template, c.email)
            }
            //隔3秒再抓取
            sleep(3000)
        }
    }
}





