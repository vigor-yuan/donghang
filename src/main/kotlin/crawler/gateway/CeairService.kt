package crawler.gateway

import crawler.Constants
import crawler.domain.FlightSearchResp
import crawler.domain.P
import crawler.domain.SearchCond
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.network.Network
import java.net.URLDecoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * 东航接口
 */
class CeairService {

    //初始值
    var seriesId: String? = null

    //cookie
    var cookie: String = ""

    init {
        getSeriesIdAndCookie()
    }

    companion object {
        val ceairService = CeairService()
    }

    /**
     * 搜索航班接口
     */
    fun doSearch(
        searchCond: SearchCond
    ): FlightSearchResp? {
        searchCond.seriesid = this.seriesId ?: throw RuntimeException("未捕获到seriesId")
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
                    "http://www.ceair.com/booking/sha-pek-${OffsetDateTime.now().plusDays(1)
                        .format(DateTimeFormatter.ofPattern("yyMMdd"))}_CNY.html"
                ),
                Pair("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7"),
                Pair(
                    "Cookie", this.cookie
                )
            ),
            "_=${this.seriesId}&searchCond=${searchCond.toJson()}"
        ).exec(FlightSearchResp::class.java);
    }

    private fun getSeriesIdAndCookie() {
        //拿到seriesId 和 cookie
//        System.setProperty("webdriver.chrome.driver", this::class.java.getResource("/chromedriver").toURI().path);
        val options = ChromeOptions().apply {
            //设置无头
            setHeadless(true)
            addArguments(
                "--no-sandbox",
                "--disable-gpu",
                "--disable-images",
                "--user-agent = Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
            )
        }
        val driver = ChromeDriver(options)
        driver.devTools.apply {
            //从链接中获取seriesId
            createSession()
            send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
            addListener(Network.requestWillBeSent()) { entry ->
                entry.request.url.takeIf {
                    //判断url
                    it.startsWith("http://observer.ceair.com/ta.png?h=event&c=button&a=submit&l=mainProcessFlightSearch")
                }?.let {
                    //解码
                    URLDecoder.decode(it, "UTF-8").substringAfter("p=")
                }?.let {
                    //转json获取seriesId
                    Constants.gson.fromJson(it, P::class.java).seriesid
                }?.also {
                    //暂存
                    println("解析出来的seriesId = $it")
                    this@CeairService.seriesId = it
                }
            }
        }
        driver["http://www.ceair.com/booking/sha-pek-${OffsetDateTime.now().plusDays(1)
            .format(DateTimeFormatter.ofPattern("yyMMdd"))}_CNY.html"]
        driver.manage().cookies.joinToString(separator = "; ", transform = { "${it.name}=${it.value}" }).also {
            print("捕获到的cookie${it}")
            this.cookie = it
        }
        //
        Thread.sleep(10000)
        // 5.退出浏览器
        driver.quit()
    }

}

