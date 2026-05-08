package dam_A15316.catapiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dam_A15316.catapiapp.adapter.CatAdapter
import dam_A15316.catapiapp.databinding.ActivityMainBinding
import dam_A15316.catapiapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure SwipeRefreshLayout colours to match the app theme
        binding.swipeRefreshLayout.setColorSchemeResources(
            com.google.android.material.R.color.design_default_color_primary
        )

        setupRecyclerView()
        observeViewModel()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCatImages()
        }
    }

    private fun setupRecyclerView() {
        adapter = CatAdapter()
        binding.recyclerViewCats.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.catImages.collectLatest { images ->
                adapter.submitList(images)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.swipeRefreshLayout.isRefreshing = isLoading
            }
        }

        lifecycleScope.launch {
            viewModel.error.collectLatest { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(this@MainActivity, "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
