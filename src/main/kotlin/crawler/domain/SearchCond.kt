package crawler.domain

import crawler.gson

data class P(val seriesid: String)

data class SearchCond(
    val segmentList: List<Segment>,
    var seriesid: String = "",
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
        return gson.toJson(this)
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

) {
    companion object {
        fun fromConfig(c: SegmentConfig): Segment {
            return Segment(c.arrCd, c.arrCdTxt, c.arrCityCode, c.deptCd, c.deptCdTxt, c.deptCityCode, c.deptDt)
        }
    }
}