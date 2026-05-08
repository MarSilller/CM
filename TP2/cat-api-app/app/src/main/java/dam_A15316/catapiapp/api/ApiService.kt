package dam_A15316.catapiapp.api

import dam_A15316.catapiapp.model.CatImage
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/images/search")
    suspend fun getCatImages(
        @Query("limit") limit: Int = 20,
        @Query("has_breeds") hasBreeds: Int = 1
    ): List<CatImage>
}
