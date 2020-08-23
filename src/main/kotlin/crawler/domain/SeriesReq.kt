package crawler.domain

import crawler.Constants.gson

private data class Seriesid(val seriesid: String)

data class SeriesReq(
    var p: String,
    val h: String = "event",
    val c: String = "button",
    val l: String = "mainProcessFlightSearch"

) {
    fun toMap(): Map<String, String> {
        return mapOf(
            Pair("p", gson.toJson(Seriesid(p))),
            Pair("h", h),
            Pair("c", c),
            Pair("l", l)
        )
    }
}

