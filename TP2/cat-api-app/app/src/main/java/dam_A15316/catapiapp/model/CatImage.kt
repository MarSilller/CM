package dam_A15316.catapiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cat_images")
data class CatImage(
    @PrimaryKey val id: String,
    val url: String,
    val width: Int?,
    val height: Int?,
    val breeds: List<Breed>?,
    var isFavorite: Boolean = false,
    var favoriteTimestamp: Long = 0L
) : Serializable
