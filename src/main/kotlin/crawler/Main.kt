package crawler

import crawler.domain.FlightSearchResp
import crawler.domain.SearchCond
import crawler.domain.Segment
import crawler.domain.SeriesReq
import crawler.gateway.Curl
import crawler.gateway.ICeairService.Companion.ceairService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.UUID.randomUUID


fun main() {
    ceairService.getSeriesId(SeriesReq("d45be34e48011eab7a46d5462ac55af").toMap()).execute()
    val seriesid = randomUUID().uuid()
    runBlocking {
        delay(2000)
    }
    val resp = ceairService.getSeriesId(SeriesReq("d46b2580e48011eab7a46d5462ac55af").toMap()).execute()
    val failed = resp.headers()["set-cookie"].isNullOrBlank()
    if (failed) {
        println("注册seriesId失败")
        //TODO @Mars 发送邮件
        return
    }
    println("seriesId$seriesid")

    runBlocking {
        delay(4000)
        val resp = doSearch(
            "d46b2580e48011eab7a46d5462ac55af",
            SearchCond(
                "d46b2580e48011eab7a46d5462ac55af",
                listOf(Segment("KWE", "贵阳", "KWE", "SHA", "上海", "SHA", "2020-08-29"))
            ).toJson()
        )
        println(resp)
    }

    runBlocking {
        //启动了一个协程，并阻塞当前线程
        delay(2000)
    }
}

suspend fun doSearch(
    seriesid: String,
    searchCond: String
): FlightSearchResp? {
    return Curl(
        "http://www.ceair.com/otabooking/flight-search!doFlightSearch.shtml",
        mapOf(
            Pair("Connection", "keep-alive"),
            Pair("Accept", "application/json, text/javascript"),
            Pair("X-Requested-With", "XMLHttpRequest"),
            Pair(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36"
            ),
            Pair("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"),
            Pair("Origin", "http://www.ceair.com"),
            Pair(
                "Referer",
                "http://www.ceair.com/booking/sha-kwe-200829_CNY.html?seriesid=$seriesid"
            ),
            Pair("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
        ),
        "_=$seriesid&searchCond=$searchCond",
        "Webtrends=805a26e5.5a8693746a872; language=zh_CN; ecrmWebtrends=210.22.83.142.1592544746186; gr_user_id=d324a973-ee63-45fe-ad08-0e7191ee0be5; _ga=GA1.2.317681491.1592544729; user_ta_session_id=186a6be2-dcf4-4d50-87af-9e36051d03b7; grwng_uid=1e96c467-4d82-4c3a-8a03-3d00eeced5b2; ffpno=13093687239; passportId=824E2E601CD690D923E2491ED029444E; 84bb15efa4e13721_gr_last_sent_cs1=824E2E601CD690D923E2491ED029444E; ssxmod_itna=QqjxBDRDyGIxnDlSx+obxcDRxhsvvxPjhYwb3D/R+DnqD=GFDK40EAB5qD7+i9MjuG+W79hS2w1EnjkopmdaiOi3eaMa4iTD4q07Db4GkDAqiOD7kbm2vrqDPDbxYPDGDKGCDbq0CXxipF9xDWAxi1la5D5h5HDDydSga+eAW36p91+gPDUe2Ddx8eZn0vFrh4DEhq4gqLNn0YFGDPYi0t3A0DF7h4HQSGgUK44D; ssxmod_itna2=QqjxBDRDyGIxnDlSx+obxcDRxhsvvxPjhYwxG9iEKzQADBuiuD7P3D7AUkshx8hRw87iCTxie8UhDyrGOY8w4hmTEABIxAKo1qqy8vz/kBG17hFk0WmMFbPIuEa1+zk08qwvs06fg65btg5PkrBHR1uTYIFsQU9sfOW8tOyGcCE+u=0IcA0q0ByNK6cbx15QNQnNzPHdcWKK8xuY2IqCa=0EeAoK9Gfr=xq4Kc8FjiQxy+uza6uNMbKnUYOiqh2t7iG4XeqE/ZDCGcSC9hy4ShKbVm0=WjuDYDnD/pZn9oYWvN6zb9BhFWCjmcK+rYDGIaf/2bAW4ueORbOYw29qg7PPAeMQFkbbjRpvfeePggDTT+P4+HQ7xuAcX+aPzRqIanF5Q0RARBRf5QnDLQGIfHjeSLFFniK7DKlf20/h402R8bF9G4Dp+Rql0haGgjBoEGF5bWg+p5j9DAEYAanQ3k743D07jGq8BItEfdgE9OH3gIMBUs4591dgBT87ouWkGDqU2XKFNyTqqDDLxD2n3az04AhDD===; _gid=GA1.2.1788686983.1597987205; 84bb15efa4e13721_gr_session_id=a2d32f4d-2548-4ab7-8f18-3f1c4a8bb685; 84bb15efa4e13721_gr_last_sent_sid_with_cs1=a2d32f4d-2548-4ab7-8f18-3f1c4a8bb685; _gat_UA-80008755-11=1; 84bb15efa4e13721_gr_session_id_a2d32f4d-2548-4ab7-8f18-3f1c4a8bb685=true; 84bb15efa4e13721_gr_cs1=824E2E601CD690D923E2491ED029444E; JSESSIONID=Ku9PxDW8L81c89ZOwCvC3iL5.laputaServer3; _gat=1"
    )
        .exec(FlightSearchResp::class.java);
}




