package crawler

import com.google.gson.Gson
import java.util.*

fun UUID.uuid(): String {
    return this.toString().replace("-", "");
}

object Constants {
    //gson
    val gson: Gson = Gson()
    val seriesId: String = ""
}