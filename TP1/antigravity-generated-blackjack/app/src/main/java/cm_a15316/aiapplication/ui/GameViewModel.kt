package cm_a15316.aiapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cm_a15316.aiapplication.data.PreferencesManager
import cm_a15316.aiapplication.domain.GameEngine
import cm_a15316.aiapplication.domain.GameState
import cm_a15316.aiapplication.domain.GameUIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        var testEngine: GameEngine? = null
    }

    private val preferencesManager = PreferencesManager(application)
    private val gameEngine = testEngine ?: GameEngine()

    val uiState: StateFlow<GameUIState> = gameEngine.uiState

    init {
        viewModelScope.launch {
            preferencesManager.chipBalanceFlow.collectLatest { balance ->
                gameEngine.syncBalance(balance)
            }
        }

        viewModelScope.launch {
            gameEngine.uiState.collectLatest { state ->
                if (state.gameState == GameState.RESOLVED) {
                    preferencesManager.updateChipBalance(state.chipBalance)
                }
            }
        }
    }

    fun placeBet(amount: Int) {
        gameEngine.placeBet(amount)
    }

    fun deal() {
        gameEngine.deal()
    }

    fun hit() {
        gameEngine.hit()
    }

    fun stand() {
        gameEngine.stand()
    }

    fun nextHand() {
        gameEngine.resetForNextHand()
    }
}
