package cm_a15316.helloworld

import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
        println ( this@MainActivity . localClassName + " onCreate ")
        val image1 = findViewById<ImageView>(R.id.imageView)
        val image2 = findViewById<ImageView>(R.id.imageView2)
        val image3 = findViewById<ImageView>(R.id.imageView3)
        val image4 = findViewById<ImageView>(R.id.imageView4)

        val numbers = listOf( //list with png numbers we have on drawable
            R.drawable.num1,
            R.drawable.num2,
            R.drawable.num3,
            R.drawable.num4,
            R.drawable.num5,
            R.drawable.num6,
            R.drawable.num7
        )

        fun randomizeImages() {
            val randomImages = numbers.shuffled() //shuffle's list so the now all the index are mixed up

            image2.setImageResource(randomImages[0]) //this will not select num1, because of the shuffle the index 0 should be a different number (or not depending on the shuffle of course)
            image3.setImageResource(randomImages[1])
            image4.setImageResource(randomImages[2])
        }

        image1.setOnClickListener {
            image1.visibility = View.INVISIBLE
            image2.visibility = View.VISIBLE
            image3.visibility = View.VISIBLE
            image4.visibility = View.VISIBLE

            randomizeImages()
        }

        image4.setOnClickListener {
            randomizeImages()
        }


    }

}
