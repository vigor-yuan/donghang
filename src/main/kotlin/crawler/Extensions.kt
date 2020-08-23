package crawler

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

data class ParameterizedTypeImpl(
    val raw: Class<*>,
    val args: Array<Type?>
) :
    ParameterizedType {
    override fun getRawType(): Type? {
        return this.raw;
    }

    override fun getOwnerType(): Type? {
        return null;
    }

    override fun getActualTypeArguments(): Array<Type?> {
        return args;
    }
}

fun UUID.uuid(): String {
    return this.toString().replace("-", "");
}

object Constants {
    //gson
    val gson: Gson = Gson()
}