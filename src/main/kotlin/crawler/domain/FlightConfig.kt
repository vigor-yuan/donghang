package crawler.domain

import crawler.Constants
import java.io.File

//抓取航线配置
data class FlightConfig(
    val config: List<PersonConfig>
) {
    companion object {
        fun read(): FlightConfig {
            return try {
                File("config.json").bufferedReader().let {
                    Constants.gson.fromJson<FlightConfig>(it, FlightConfig::class.java)
                }.also {
                    println("读取的配置信息$it")
                }
            } catch (e: Exception) {
                println(e)
                throw e
            }
        }
    }
}

//人员配置
data class PersonConfig(
    val email: String,
    val name: String,
    val segmentList: List<SegmentConfig>
)

//需要抓取的航段配置
data class SegmentConfig(
    val arrCd: String,
    val arrCdTxt: String,
    val arrCityCode: String,
    val deptCd: String,
    val deptCdTxt: String,
    val deptCityCode: String,
    val deptDt: String,
    //航段的航班配置
    val flightNos: List<String>
)