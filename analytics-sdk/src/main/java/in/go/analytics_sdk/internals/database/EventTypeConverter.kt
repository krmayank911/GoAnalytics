package `in`.go.analytics_sdk.internals.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventTypeConverter {
    private val gson = Gson()
    private val mapType = object : TypeToken<Map<String, Any>>() {}.type

    @TypeConverter
    fun fromMap(map: Map<String, Any>?): String? {
        if (map == null) return null
        return gson.toJson(map, mapType)
    }

    @TypeConverter
    fun toMap(json: String?): Map<String, Any>? {
        if (json.isNullOrBlank()) return emptyMap()
        return gson.fromJson(json, mapType)
    }
}