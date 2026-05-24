package dam_A15316.coolweatherapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData (
    var latitude : Float ,
    var longitude : Float ,
    var timezone : String ,
    var current_weather : CurrentWeather ,
    var hourly : Hourly
)


@Serializable
data class CurrentWeather (
    var temperature : Float ,
    val windspeed : Float ,
    val winddirection: Float ,
    var weathercode : Int ,
    var time : String
)

@Serializable
data class Hourly (
    var time : List < String >,
    var temperature_2m : List < Float >,
    var weathercode : List < Int >,
    var pressure_msl : List < Double >
)
enum class WMO_WeatherCode ( var code : Int , var image : String ) {
    CLEAR_SKY (0 , " clear_ ") ,
    MAINLY_CLEAR (1 , " mostly_clear_ ") ,
    PARTLY_CLOUDY (2 , " partly_cloudy_ ") ,
    OVERCAST (3 , " cloudy ") ,
    FOG (45 , " fog ") ,
    DEPOSITING_RIME_FOG (48 , " fog ") ,
    DRIZZLE_LIGHT (51 , " drizzle ") ,
    DRIZZLE_MODERATE (53 , " drizzle ") ,
    DRIZZLE_DENSE (55 , " drizzle ") ,
    FREEZING_DRIZZLE_LIGHT (56 , " freezing_drizzle ") ,
    FREEZING_DRIZZLE_DENSE (57 , " freezing_drizzle ") ,
    RAIN_SLIGHT (61 , " rain_light ") ,
    RAIN_MODERATE (63 , " rain ") ,
    RAIN_HEAVY (65 , " rain_heavy ") ,
    FREEZING_RAIN_LIGHT (66 , " freezing_rain_light ") ,
    FREEZING_RAIN_HEAVY (67 , " freezing_rain_heavy ") ,
    SNOW_FALL_SLIGHT (71 , " snow_light ") ,
    SNOW_FALL_MODERATE (73 , " snow ") ,
    SNOW_FALL_HEAVY (75 , " snow_heavy ") ,
    SNOW_GRAINS (77 , " snow ") ,
    RAIN_SHOWERS_SLIGHT (80 , " rain_light ") ,
    RAIN_SHOWERS_MODERATE (81 , " rain ") ,
    RAIN_SHOWERS_VIOLENT (82 , " rain_heavy ") ,
    SNOW_SHOWERS_SLIGHT (85 , " snow_light ") ,
    SNOW_SHOWERS_HEAVY (86 , " snow_heavy ") ,
    THUNDERSTORM_SLIGHT_MODERATE (95 , " tstorm ") ,
    THUNDERSTORM_HAIL_SLIGHT (96 , " tstorm ") ,
    THUNDERSTORM_HAIL_HEAVY (99 , " tstorm ")
}

fun getWeatherCodeMap () : Map < Int , WMO_WeatherCode > {
    var weatherMap = HashMap < Int , WMO_WeatherCode >()
    WMO_WeatherCode . values () . forEach {
        weatherMap . put ( it . code , it )
    }
    return weatherMap
}


object WeatherApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getWeather ( lat : Float , lon : Float ): WeatherData ? {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${lon}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m")
        }
        System.out.println("Getting URL: $reqString")
        return try {
            client.get(reqString).body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}