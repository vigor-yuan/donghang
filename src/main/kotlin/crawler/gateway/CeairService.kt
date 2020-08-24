package crawler.gateway

import crawler.Constants
import crawler.domain.FlightSearchResp
import crawler.domain.P
import crawler.domain.SearchCond
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.network.Network
import java.net.URLDecoder
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
        //初始化拿到seriesID
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        ChromeOptions().apply {
            addArguments(
                "--no-sandbox",
                "--disable-images",
                "--user-agent = Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0.2 Safari/604.4.7"
            )
        }
        val driver = ChromeDriver()
        driver.devTools.apply {
            createSession()
            send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
            addListener(Network.requestWillBeSent()) { entry ->
                entry.request.url.takeIf { it.startsWith("http://observer.ceair.com/ta.png?h=event&c=button&a=submit&l=mainProcessFlightSearch") }
                    ?.let {
                        URLDecoder.decode(it, "UTF-8").substringAfter("p=").let {
                            Constants.gson.fromJson(it, P::class.java)
                        }.also {
                            println("解析出来的seriesId = ${it.seriesid}")
                            //保存id
                            this@CeairService.apply {
                                seriesId = it.seriesid
                            }
                        }
                    }
            }
        }
        driver["http://www.ceair.com/booking/sha-pek-200824_CNY.html"]
        driver.manage().cookies.joinToString(separator = "; ", transform = {
            "${it.name}=${it.value}"
        }).also {
            print("捕获到的cookie${it}")
            this.cookie = it;
        }
        Thread.sleep(10000)
        // 5.退出浏览器
        driver.quit()
    }

    companion object {
        val ceairService = CeairService()
    }

    /**
     * 搜索接口
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
                    "http://www.ceair.com/booking/sha-pek-200824_CNY.html"
                ),
                Pair("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7"),
                Pair(
                    "Cookie",
                    this.cookie
                )
            ),
            "_=${this.seriesId}&searchCond=${searchCond.toJson()}"
//            "_=${this.seriesId}&searchCond={\"adtCount\":1,\"chdCount\":0,\"infCount\":0,\"currency\":\"CNY\",\"tripType\":\"OW\",\"recommend\":false,\"reselect\":\"\",\"page\":\"0\",\"sortType\":\"a\",\"sortExec\":\"a\",\"seriesid\":\"${this.seriesId}\",\"segmentList\":[{\"deptCd\":\"SHA\",\"arrCd\":\"PEK\",\"deptDt\":\"2020-08-24\",\"deptAirport\":\"\",\"arrAirport\":\"\",\"deptCdTxt\":\"上海\",\"arrCdTxt\":\"北京\",\"deptCityCode\":\"SHA\",\"arrCityCode\":\"BJS\"}],\"version\":\"A.1.0\"}"
        ).exec(FlightSearchResp::class.java);
    }


}

