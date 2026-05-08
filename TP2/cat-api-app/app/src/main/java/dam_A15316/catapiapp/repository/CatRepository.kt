package dam_A15316.catapiapp.repository

import android.util.Log
import dam_A15316.catapiapp.api.RetrofitInstance
import dam_A15316.catapiapp.database.CatDao
import dam_A15316.catapiapp.model.CatImage

class CatRepository(private val catDao: CatDao) {
    suspend fun getCatImages(): List<CatImage> {
        return try {
            val remoteImages = RetrofitInstance.api.getCatImages()
            Log.d("CatAPI", "Fetched cats from API. First cat breeds: ${remoteImages.firstOrNull()?.breeds}")
            if (remoteImages.isNotEmpty()) {
                catDao.insertCats(remoteImages)
                catDao.clearOldCache()
            }
            catDao.getCachedCats()
        } catch (e: Exception) {
            Log.e("CatRepository", "Network fetch failed, falling back to cache.", e)
            val cached = catDao.getCachedCats()
            if (cached.isEmpty()) {
                throw Exception("No internet connection and no cached data available.")
            }
            cached
        }
    }

    suspend fun getCatById(id: String): CatImage? {
        return catDao.getCatById(id)
    }

    suspend fun toggleFavorite(catId: String, makeFavorite: Boolean) {
        if (makeFavorite) {
            val count = catDao.getFavoritesCount()
            if (count >= 5) {
                // FIFO logic: remove oldest favorite
                val oldestId = catDao.getOldestFavoriteId()
                if (oldestId != null) {
                    catDao.updateFavoriteStatus(oldestId, isFavorite = false, timestamp = 0L)
                }
            }
            catDao.updateFavoriteStatus(catId, isFavorite = true, timestamp = System.currentTimeMillis())
        } else {
            catDao.updateFavoriteStatus(catId, isFavorite = false, timestamp = 0L)
        }
    }
}
