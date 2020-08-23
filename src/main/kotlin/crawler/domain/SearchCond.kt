package crawler.domain

import com.google.gson.annotations.SerializedName
import crawler.Constants

data class SearchReq(
    @SerializedName("_") val seriesid: String,
    val searchCond: SearchCond
)

data class SearchCond(
    val seriesid: String,
    val segmentList: List<Segment>,
    val adtCount: Int = 1,
    val chdCount: Int = 0,
    val currency: String = "CNY",
    val infCount: Int = 0,
    val page: String = "0",
    val recommend: Boolean = false,
    val reselect: String = "",
    val sortExec: String = "a",
    val sortType: String = "a",
    val tripType: String = "OW",
    val version: String = "A.1.0"
) {
    fun toJson(): String {
        return Constants.gson.toJson(this)
    }
}

data class Segment(
    /**
     * 到达城市
     */
    val arrCd: String,
    /**
     * 到达城市
     */
    val arrCdTxt: String,
    /**
     * 到达城市编号
     */
    val arrCityCode: String,
    /**
     * 出发城市编号
     */
    val deptCd: String,
    /**
     * 出发城市文字
     */
    val deptCdTxt: String,
    /**
     * 出发城市编号
     */
    val deptCityCode: String,
    /**
     * 出发日期
     */
    val deptDt: String,
    /**
     * 出发机场
     */
    val deptAirport: String = "",
    /**
     * 到达机场
     */
    val arrAirport: String = ""
)