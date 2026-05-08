package dam_A15316.catapiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dam_A15316.catapiapp.database.CatDatabase
import dam_A15316.catapiapp.model.CatImage
import dam_A15316.catapiapp.repository.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val catDao = CatDatabase.getDatabase(application).catDao()
    private val repository = CatRepository(catDao)
    
    private val _catImages = MutableStateFlow<List<CatImage>>(emptyList())
    val catImages: StateFlow<List<CatImage>> = _catImages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchCatImages()
    }

    fun fetchCatImages() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val images = repository.getCatImages()
                _catImages.value = images
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(catId: String, makeFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(catId, makeFavorite)
            // Refresh list to update UI state if needed
            fetchCatImages()
        }
    }
}
