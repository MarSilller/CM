package dam_A15316.catapiapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dam_A15316.catapiapp.model.Breed

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromBreedList(breeds: List<Breed>?): String {
        return gson.toJson(breeds)
    }

    @TypeConverter
    fun toBreedList(breedsString: String?): List<Breed>? {
        if (breedsString == null) return null
        val type = object : TypeToken<List<Breed>>() {}.type
        return gson.fromJson(breedsString, type)
    }
}
