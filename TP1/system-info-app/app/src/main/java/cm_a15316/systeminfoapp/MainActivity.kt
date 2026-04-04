package cm_a15316.systeminfoapp


import android.os.Build //import added to access the system info
import android.os.Bundle
import android.widget.TextView //import added to manipulate TextViews
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val systemInfoTextView = findViewById<TextView>(R.id.systemInfo) //find the TextView on the layout

        val systemInfo = //create string with all the info
                "Manufacturer: ${Build.MANUFACTURER}\nModel: ${Build.MODEL}" +
                "\nBrand: ${Build.BRAND}\nType: ${Build.TYPE}" +
                "\nUser: ${Build.USER}\nBase: ${Build.VERSION.BASE_OS}" +
                "\nIncremental: ${Build.VERSION.INCREMENTAL}\nSDK: ${Build.VERSION.SDK_INT}" +
                "\nVersion Code: ${Build.VERSION.RELEASE}\nDisplay: ${Build.DISPLAY}"

        systemInfoTextView.text = systemInfo //set the text in the TextView as the system info just gotten
    }
}