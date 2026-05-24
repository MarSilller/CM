package dam_A15316.coolweatherapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dam_A15316.coolweatherapp.ui.WeatherUI

class MainActivity : ComponentActivity() {

    private var theme = true // true = light, false = dark

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            theme = savedInstanceState.getBoolean("theme")
        }

        val tablet = resources.configuration.smallestScreenWidthDp >= 720
        val land = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val setThemeVar = when {
            tablet && land && theme -> R.style.Theme_Day_Land_Tablet
            tablet && !land && theme -> R.style.Theme_Day_Tablet
            tablet && land && !theme -> R.style.Theme_Night_Land_Tablet
            tablet && !land && !theme -> R.style.Theme_Night_Tablet
            !tablet && land && theme -> R.style.Theme_Day_Land
            !tablet && !land && theme -> R.style.Theme_Day
            !tablet && land && !theme -> R.style.Theme_Night_Land
            else -> R.style.Theme_Night
        }

        setTheme(setThemeVar)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Light Theme: ", modifier = Modifier.padding(end = 8.dp))
                        Switch(
                            checked = theme,
                            onCheckedChange = { isChecked ->
                                theme = isChecked
                                recreate() // Safely triggers configuration updates and theme resource swaps
                            }
                        )
                    }
                    WeatherUI()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("theme", theme)
    }
}