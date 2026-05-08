package dam_A15316.catapiapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dam_A15316.catapiapp.CatDetailActivity
import dam_A15316.catapiapp.databinding.ItemCatBinding
import dam_A15316.catapiapp.model.CatImage

class CatAdapter : ListAdapter<CatImage, CatAdapter.CatViewHolder>(CatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CatViewHolder(private val binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(catImage: CatImage) {
            Glide.with(binding.root.context)
                .load(catImage.url)
                .centerCrop()
                .into(binding.imageCat)
                
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, CatDetailActivity::class.java)
                intent.putExtra("CAT_ID", catImage.id)
                binding.root.context.startActivity(intent)
            }
        }
    }

    class CatDiffCallback : DiffUtil.ItemCallback<CatImage>() {
        override fun areItemsTheSame(oldItem: CatImage, newItem: CatImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatImage, newItem: CatImage): Boolean {
            return oldItem == newItem
        }
    }
}
