package dam_A15316.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import dam_A15316.coolweatherapp.R
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_A15316.coolweatherapp.WMO_WeatherCode
import dam_A15316.coolweatherapp.getWeatherCodeMap

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()
    val latitude = weatherUIState.latitude
    val longitude = weatherUIState.longitude
    val temperature = weatherUIState.temperature
    val windSpeed = weatherUIState.windspeed
    val windDirection = weatherUIState.winddirection
    val weathercode = weatherUIState.weathercode
    val seaLevelPressure = weatherUIState.seaLevelPressure
    val time = weatherUIState.time
    val configuration = LocalConfiguration.current
    val day = true
    val mapt = getWeatherCodeMap()
    val wCode = mapt[weathercode]
    val wImage = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode.image + "day" else wCode.image + "night"
        else -> wCode?.image
    }
    val context = LocalContext.current

    val cleanImageName = wImage?.replace(" ", "")?.trim() ?: "fog"
    val wIcon = context.resources.getIdentifier(cleanImageName, "drawable", context.packageName)

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            wIcon = wIcon, latitude = latitude, longitude = longitude, temperature = temperature,
            windSpeed = windSpeed, windDirection = windDirection.toInt(), weathercode = weathercode,
            seaLevelPressure = seaLevelPressure, time = time,
            onLatitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) } },
            onLongitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) } },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    } else {
        PortraitWeatherUI(
            wIcon = wIcon, latitude = latitude, longitude = longitude, temperature = temperature,
            windSpeed = windSpeed, windDirection = windDirection.toInt(), weathercode = weathercode,
            seaLevelPressure = seaLevelPressure, time = time,
            onLatitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) } },
            onLongitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) } },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int, latitude: Float, longitude: Float, temperature: Float, windSpeed: Float,
    windDirection: Int, weathercode: Int, seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit, onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (wIcon != 0) {
            Image(
                painter = painterResource(id = wIcon),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(140.dp) // Set explicit larger size for portrait layout
                    .padding(bottom = 16.dp)
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = latitude.toString(), onValueChange = onLatitudeChange, label = { Text(stringResource(id = R.string.lat))})
                OutlinedTextField(value = longitude.toString(), onValueChange = onLongitudeChange, label = { Text(stringResource(id = R.string.lon)) })

                Text(text = "${stringResource(id = R.string.temp)}: $temperature °C")
                Text(text = "${stringResource(id = R.string.winds)}: $windSpeed km/h")
                Text(text = "${stringResource(id = R.string.windd)}: $windDirection°")
                Text(text = "${stringResource(id = R.string.sea)}: $seaLevelPressure hPa")
                Text(text = "${stringResource(id = R.string.time)}: $time")
            }
        }

        Button(onClick = onUpdateButtonClick, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(id = R.string.but))
        }
    }
}

@Composable
fun LandscapeWeatherUI(
    wIcon: Int, latitude: Float, longitude: Float, temperature: Float, windSpeed: Float,
    windDirection: Int, weathercode: Int, seaLevelPressure: Float, time: String,
    onLatitudeChange: (String) -> Unit, onLongitudeChange: (String) -> Unit, onUpdateButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 16.dp)) {
            if (wIcon != 0) {
                Image(
                    painter = painterResource(id = wIcon),
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(100.dp) // Set landscape size comfortable for horizontal split screen layout
                        .padding(bottom = 8.dp)
                )
            }
            Button(onClick = onUpdateButtonClick) { Text(stringResource(id = R.string.but)) }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(value = latitude.toString(), onValueChange = onLatitudeChange, label = { Text(stringResource(id = R.string.lat)) })
                OutlinedTextField(value = longitude.toString(), onValueChange = onLongitudeChange, label = { Text(stringResource(id = R.string.lon)) })
                Text(text = "${stringResource(id = R.string.temp)}: $temperature °C")
                Text(text = "${stringResource(id = R.string.winds)}: $windSpeed km/h")
                Text(text = "${stringResource(id = R.string.windd)}: $windDirection°")
                Text(text = "${stringResource(id = R.string.sea)}: $seaLevelPressure hPa")
                Text(text = "${stringResource(id = R.string.time)}: $time")
            }
        }
    }
}