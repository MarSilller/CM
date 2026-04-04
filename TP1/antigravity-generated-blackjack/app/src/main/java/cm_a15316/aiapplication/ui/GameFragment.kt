package cm_a15316.aiapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cm_a15316.aiapplication.databinding.FragmentGameBinding
import cm_a15316.aiapplication.domain.GameResult
import cm_a15316.aiapplication.domain.GameState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private lateinit var dealerAdapter: CardAdapter
    private lateinit var playerAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        dealerAdapter = CardAdapter(isDealerHidden = true)
        playerAdapter = CardAdapter()

        val overlapWidth = (resources.displayMetrics.density * 50).toInt()
        val itemDecoration = OverlapItemDecoration(overlapWidth)

        binding.rvDealerCards.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dealerAdapter
            addItemDecoration(itemDecoration)
        }

        binding.rvPlayerCards.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playerAdapter
            addItemDecoration(itemDecoration)
        }
    }

    private fun setupClickListeners() {
        binding.btnBet10.setOnClickListener { viewModel.placeBet(10) }
        binding.btnDeal.setOnClickListener {
            if (binding.btnDeal.text == "Next Hand") {
                viewModel.nextHand()
            } else {
                viewModel.deal()
            }
        }
        binding.btnHit.setOnClickListener { viewModel.hit() }
        binding.btnStand.setOnClickListener { viewModel.stand() }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.tvChipBalance.text = "Balance: $${state.chipBalance}"
                binding.tvCurrentBet.text = "Bet: $${state.currentBet}"

                val isDealerHidden =
                    state.gameState == GameState.PLAYER_TURN || state.gameState == GameState.BETTING

                dealerAdapter.setDealerHidden(isDealerHidden)
                dealerAdapter.submitList(state.dealerHand.cards)
                playerAdapter.submitList(state.playerHand.cards)

                when (state.gameState) {
                    GameState.BETTING -> {
                        binding.btnBet10.isEnabled = true
                        binding.btnDeal.isEnabled = state.currentBet > 0
                        binding.btnDeal.text = "Deal"
                        binding.btnHit.isEnabled = false
                        binding.btnStand.isEnabled = false
                        binding.tvGameResult.visibility = View.GONE
                    }
                    GameState.PLAYER_TURN -> {
                        binding.btnBet10.isEnabled = false
                        binding.btnDeal.isEnabled = false
                        binding.btnHit.isEnabled = true
                        binding.btnStand.isEnabled = true
                        binding.tvGameResult.visibility = View.GONE
                    }
                    GameState.DEALER_TURN -> {
                        binding.btnBet10.isEnabled = false
                        binding.btnDeal.isEnabled = false
                        binding.btnHit.isEnabled = false
                        binding.btnStand.isEnabled = false
                        binding.tvGameResult.visibility = View.GONE
                    }
                    GameState.RESOLVED -> {
                        binding.btnBet10.isEnabled = false
                        binding.btnDeal.isEnabled = true
                        binding.btnDeal.text = "Next Hand"
                        binding.btnHit.isEnabled = false
                        binding.btnStand.isEnabled = false

                        binding.tvGameResult.visibility = View.VISIBLE
                        binding.tvGameResult.text = when (state.result) {
                            GameResult.PLAYER_WIN -> "Player Wins!"
                            GameResult.DEALER_WIN -> "Dealer Wins!"
                            GameResult.PUSH -> "Push!"
                            GameResult.PLAYER_BLACKJACK -> "Blackjack!"
                            GameResult.NONE -> ""
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
