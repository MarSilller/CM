package dam_A15316.catapiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dam_A15316.catapiapp.databinding.ActivityCatDetailBinding
import dam_A15316.catapiapp.model.CatImage
import dam_A15316.catapiapp.viewmodel.MainViewModel
import dam_A15316.catapiapp.database.CatDatabase
import kotlinx.coroutines.launch

class CatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private var currentCatId: String? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentCatId = intent.getStringExtra("CAT_ID")
        
        if (currentCatId == null) {
            Toast.makeText(this, "Cat ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        loadCatDetails()

        binding.fabFavorite.setOnClickListener {
            currentCatId?.let { id ->
                isFavorite = !isFavorite
                viewModel.toggleFavorite(id, isFavorite)
                updateFavoriteIcon()
            }
        }
    }

    private fun loadCatDetails() {
        lifecycleScope.launch {
            currentCatId?.let { id ->
                // Fetch directly from DB since it's cached
                val catDao = CatDatabase.getDatabase(this@CatDetailActivity).catDao()
                val catImage = catDao.getCatById(id)
                
                if (catImage != null) {
                    displayCat(catImage)
                } else {
                    Toast.makeText(this@CatDetailActivity, "Could not load details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayCat(cat: CatImage) {
        Glide.with(this)
            .load(cat.url)
            .into(binding.imageDetail)

        isFavorite = cat.isFavorite
        updateFavoriteIcon()

        val breed = cat.breeds?.firstOrNull()
        
        binding.textBreedName.text = breed?.name?.takeIf { it.isNotBlank() } ?: "Curious Cat (Domestic Shorthair)"
        binding.textOrigin.text = "Origin: ${breed?.origin?.takeIf { it.isNotBlank() } ?: "Unknown location"}"
        binding.textTemperament.text = "Temperament: ${breed?.temperament?.takeIf { it.isNotBlank() } ?: "Playful and cuddly"}"
        binding.textDescription.text = breed?.description?.takeIf { it.isNotBlank() } ?: "A beautiful and curious feline companion."
    }

    private fun updateFavoriteIcon() {
        val iconRes = if (isFavorite) android.R.drawable.star_on else android.R.drawable.star_off
        binding.fabFavorite.setImageResource(iconRes)
    }
}
