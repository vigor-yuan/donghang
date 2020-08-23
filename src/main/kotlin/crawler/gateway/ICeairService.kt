package crawler.gateway

import crawler.domain.FlightSearchResp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.net.URLDecoder


/**
 * 东航接口
 */
interface ICeairService {

    @GET("/ta.png")
    fun getSeriesId(@QueryMap options: Map<String, String>): Call<Any>

    companion object {

        fun initClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    var request = chain.request()
                    val requestBuilder = request.newBuilder()
                    request = requestBuilder.post(
                        (request.body?.let {
                            URLDecoder.decode(it.toString(), "UTF-8")
                        } ?: ""
                                ).toRequestBody("application/x-www-form-urlencoded; charset=UTF-8".toMediaTypeOrNull())
                    ).build()
                    chain.proceed(request)
                }
                .build()
        }

        val ceairService = Retrofit.Builder()
            .baseUrl("http://www.ceair.com/")
            .client(initClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ICeairService::class.java)
    }

}

