package crawler.domain

//搜索结果
data class FlightSearchResp(
    private val resultCode: String,
    private val searchProduct: List<SearchProduct> = emptyList()
)

//搜索产品
data class SearchProduct(
    val cabin: Cabin,
    val snk: String,
    private val salePrice: Double
)

//客舱
data class Cabin(val baseCabinCode: String, val cabinStatus: String)

