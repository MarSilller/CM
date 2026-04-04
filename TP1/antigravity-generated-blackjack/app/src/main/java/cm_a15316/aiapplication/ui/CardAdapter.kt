package cm_a15316.aiapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm_a15316.aiapplication.R
import cm_a15316.aiapplication.databinding.ItemCardBinding
import cm_a15316.aiapplication.domain.models.Card
import cm_a15316.aiapplication.domain.models.Suit

class CardAdapter(private var isDealerHidden: Boolean = false) :
    ListAdapter<Card, CardAdapter.CardViewHolder>(CardDiffCallback()) {

    fun setDealerHidden(hidden: Boolean) {
        if (isDealerHidden != hidden) {
            isDealerHidden = hidden
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position), position == 1 && isDealerHidden)
    }

    class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card, isHidden: Boolean) {
            if (isHidden) {
                binding.root.setBackgroundResource(R.drawable.card_back)
                binding.tvRankTop.text = ""
                binding.tvRankBottom.text = ""
                binding.ivSuitCenter.setImageDrawable(null)
            } else {
                binding.root.setBackgroundResource(R.drawable.card_background)
                val rankText = when (card.rank.name) {
                    "ACE" -> "A"
                    "KING" -> "K"
                    "QUEEN" -> "Q"
                    "JACK" -> "J"
                    else -> card.rank.value.toString()
                }
                val suitRes = when (card.suit) {
                    Suit.SPADES -> R.drawable.ic_suit_spades
                    Suit.HEARTS -> R.drawable.ic_suit_hearts
                    Suit.DIAMONDS -> R.drawable.ic_suit_diamonds
                    Suit.CLUBS -> R.drawable.ic_suit_clubs
                }
                val color = if (card.suit == Suit.HEARTS || card.suit == Suit.DIAMONDS) {
                    android.graphics.Color.RED
                } else {
                    android.graphics.Color.BLACK
                }

                binding.tvRankTop.text = rankText
                binding.tvRankTop.setTextColor(color)
                binding.tvRankBottom.text = rankText
                binding.tvRankBottom.setTextColor(color)
                binding.ivSuitCenter.setImageResource(suitRes)
            }
        }
    }

    class CardDiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem == newItem
        }
    }
}