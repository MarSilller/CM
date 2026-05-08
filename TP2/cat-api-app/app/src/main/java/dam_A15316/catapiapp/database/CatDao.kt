package dam_A15316.catapiapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dam_A15316.catapiapp.model.CatImage

@Dao
interface CatDao {
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCats(cats: List<CatImage>)

    @Query("SELECT * FROM cat_images ORDER BY ROWID DESC LIMIT 50")
    suspend fun getCachedCats(): List<CatImage>

    @Query("SELECT * FROM cat_images WHERE id = :id LIMIT 1")
    suspend fun getCatById(id: String): CatImage?

    // Keep only the latest 50 non-favorite items. 
    // Uses ROWID to delete older non-favorite entries.
    @Query("DELETE FROM cat_images WHERE isFavorite = 0 AND id NOT IN (SELECT id FROM cat_images WHERE isFavorite = 0 ORDER BY ROWID DESC LIMIT 50)")
    suspend fun clearOldCache()

    // --- Favorites Logic ---

    @Query("SELECT COUNT(*) FROM cat_images WHERE isFavorite = 1")
    suspend fun getFavoritesCount(): Int

    // Get the ID of the oldest favorite (the one with the smallest timestamp)
    @Query("SELECT id FROM cat_images WHERE isFavorite = 1 ORDER BY favoriteTimestamp ASC LIMIT 1")
    suspend fun getOldestFavoriteId(): String?

    @Query("UPDATE cat_images SET isFavorite = :isFavorite, favoriteTimestamp = :timestamp WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean, timestamp: Long)
}
