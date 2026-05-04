package dam_A15316.coolweatherapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import android.widget.Switch
import android.widget.EditText
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var theme = true // true = light, false = dark

    private val WEATHER_URL1 = "https://api.open-meteo.com/v1/forecast"
    private val WEATHER_URL2 = "&hourly=pressure_msl&current=temperature_2m,weather_code,wind_direction_10m,wind_speed_10m"
    private val WEATHER_URL3 = "&timezone=Europe%2FLondon"

    override fun onCreate(savedInstanceState: Bundle?){

        if (savedInstanceState != null) { //if theres a saved instance
            theme = savedInstanceState.getBoolean("theme") //overrite theme to match it

        }

        val tablet = resources.configuration.smallestScreenWidthDp >= 720
        val land = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val setThemeVar = when{
            tablet && land && theme -> R.style.Theme_Day_Land_Tablet
            tablet && !land && theme -> R.style.Theme_Day_Tablet

            tablet && land && !theme -> R.style.Theme_Night_Land_Tablet
            tablet && !land && !theme -> R.style.Theme_Night_Tablet

            !tablet && land && theme -> R.style.Theme_Day_Land
            !tablet && !land && theme -> R.style.Theme_Day

            !tablet && land && !theme -> R.style.Theme_Night_Land

            else -> R.style.Theme_Night //!tablet && !land && !theme
        }

        setTheme(setThemeVar)

/*HERE*/super.onCreate(savedInstanceState)//HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)


        /*if (savedInstanceState != null) {
            findViewById<TextView>(R.id.textView4).text = savedInstanceState.getString("temp")
            findViewById<TextView>(R.id.textView8).text = savedInstanceState.getString("windd")
            findViewById<TextView>(R.id.textView10).text = savedInstanceState.getString("pressure")
            findViewById<TextView>(R.id.textView6).text = savedInstanceState.getString("winds")
            findViewById<TextView>(R.id.textView2).text = savedInstanceState.getString("time")
        }*/

        val button = findViewById<Button>(R.id.button)
        val latInput = findViewById<EditText>(R.id.textView14)
        val lonInput = findViewById<EditText>(R.id.textView12)

        if (savedInstanceState == null) {
            latInput.setText("38.7223")
            lonInput.setText("-9.1393")

            fetchWeatherData(38.7223f, -9.1393f).start()
        } else {
            val savedLat = savedInstanceState.getString("saved_lat")
            val savedLon = savedInstanceState.getString("saved_lon")

            latInput.setText(savedLat)
            lonInput.setText(savedLon)

            val lat = savedLat?.toFloatOrNull()
            val lon = savedLon?.toFloatOrNull()
            if (lat != null && lon != null) {
                fetchWeatherData(lat, lon).start()
            }
        }


        button.setOnClickListener {
            val lat = latInput.text.toString().toFloatOrNull()
            val long = lonInput.text.toString().toFloatOrNull()

            if (lat != null && long != null) {
                fetchWeatherData(lat, long).start()
            }
        }

        val themeSwitch = findViewById<Switch>(R.id.switch1) //find switch

        themeSwitch.isChecked = theme //force the switch to match the current theme

        themeSwitch.setOnCheckedChangeListener{ _, isChecked -> //upon turning the swithc
            theme = isChecked //make theme match the switch

            recreate() //recreate app
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun WeatherAPI_Call(lat: Float, long: Float): WeatherData {
        val urlString = "$WEATHER_URL1?latitude=${lat}&longitude=${long}$WEATHER_URL2&current_weather=true"

        val url = URL(urlString)
        url.openStream().use{
            val request = Gson().fromJson(InputStreamReader(it ,"UTF-8"), WeatherData::class.java)
            return request

        }
    }

    private fun fetchWeatherData(lat: Float, long: Float): Thread {
        return Thread {
            val weather = WeatherAPI_Call(lat, long)
            updateUI(weather)
        }
    }

    private fun updateUI(request: WeatherData) {
        runOnUiThread {

            val weatherImage : ImageView = findViewById(R.id.imageView)
            val windDir : TextView = findViewById<TextView>(R.id.textView8)
            val windSpeed : TextView= findViewById<TextView>(R.id.textView6)
            val temp : TextView = findViewById<TextView>(R.id.textView4)
            val pressure : TextView = findViewById<TextView>(R.id.textView10)
            val time : TextView = findViewById<TextView>(R.id.textView2)

            windDir.text = request.current_weather.winddirection.toString() + "°"
            windSpeed.text = request.current_weather.windspeed.toString() + " km/h"
            temp.text = request.current_weather.temperature.toString() + " °C"

            time.text = request.current_weather.time

            pressure.text = request.hourly.pressure_msl.get(12).toString() + " hPa"

            val weatherMap = getWeatherCodeMap()
            val wCode = weatherMap.get(request.current_weather.weathercode)

            val wImage = when (wCode) {
                WMO_WeatherCode.CLEAR_SKY, WMO_WeatherCode.MAINLY_CLEAR, WMO_WeatherCode.PARTLY_CLOUDY ->{
                    if(theme){
                        wCode?.image + "day"
                    } else {
                        wCode?.image + "night"
                    }
                }
                    else -> {
                        wCode?.image
                    }
            }
            val cleanImageName = wImage?.trim()?.replace(" ","")
            val res = getResources()
            weatherImage.setImageResource(R.drawable.fog)
            val resID = res.getIdentifier(cleanImageName,"drawable", packageName);
            val drawable = this.getDrawable(resID);
            weatherImage.setImageDrawable(drawable);
        }
    }

    override fun onSaveInstanceState(outState: Bundle){ //before ending program it saves the state of theme in a key called "theme" which in turn will allow the next var theme to know what the previous one was (which is useful becasue of the use of recreatee())
        super.onSaveInstanceState(outState)
        outState.putBoolean("theme", theme) //saves on a key the value of theme
        outState.putString("saved_lat", findViewById<EditText>(R.id.textView14).text.toString())
        outState.putString("saved_lon", findViewById<EditText>(R.id.textView12).text.toString())

        /*outState.putString("temp", findViewById<TextView>(R.id.textView4).text.toString())
        outState.putString("winds", findViewById<TextView>(R.id.textView6).text.toString())
        outState.putString("windd", findViewById<TextView>(R.id.textView8).text.toString())
        outState.putString("time", findViewById<TextView>(R.id.textView2).text.toString())
        outState.putString("pressure", findViewById<TextView>(R.id.textView10).text.toString())*/
    }

}