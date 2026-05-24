package dam_A15316.coolweatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_A15316.coolweatherapp.WeatherApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val latitude: Float = 38.7223f,
    val longitude: Float = -9.1393f,
    val temperature: Float = 0.0f,
    val windspeed: Float = 0.0f,
    val winddirection: Float = 0.0f,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0.0f,
    val time: String = "TextView"
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        fetchWeather()
    }

    fun updateLatitude(newLat: Float) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: Float) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    fun fetchWeather() {
        val currentLat = _uiState.value.latitude
        val currentLon = _uiState.value.longitude

        viewModelScope.launch {
            try {
                val response = WeatherApiClient.getWeather(currentLat, currentLon)
                if (response != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            temperature = response.current_weather.temperature,
                            windspeed = response.current_weather.windspeed,
                            winddirection = response.current_weather.winddirection,
                            weathercode = response.current_weather.weathercode,
                            time = response.current_weather.time,
                            seaLevelPressure = response.hourly.pressure_msl.getOrNull(12)?.toFloat() ?: 0.0f
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}