package cm_a15316.aiapplication.domain

import cm_a15316.aiapplication.domain.models.Card
import cm_a15316.aiapplication.domain.models.Deck
import cm_a15316.aiapplication.domain.models.Hand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class GameState {
    BETTING, PLAYER_TURN, DEALER_TURN, RESOLVED
}

enum class GameResult {
    PLAYER_WIN, DEALER_WIN, PUSH, PLAYER_BLACKJACK, NONE
}

data class GameUIState(
    val gameState: GameState = GameState.BETTING,
    val playerHand: Hand = Hand(),
    val dealerHand: Hand = Hand(),
    val currentBet: Int = 0,
    val result: GameResult = GameResult.NONE,
    val chipBalance: Int = 0
)

class GameEngine(private val deck: Deck = Deck(numDecks = 6)) {
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    fun placeBet(amount: Int) {
        if (_uiState.value.gameState != GameState.BETTING) return
        val currentBalance = _uiState.value.chipBalance
        if (amount > currentBalance) return

        _uiState.update {
            it.copy(
                currentBet = amount,
                chipBalance = currentBalance - amount
            )
        }
    }

    fun syncBalance(balance: Int) {
        if (_uiState.value.gameState == GameState.BETTING) {
            _uiState.update { it.copy(chipBalance = balance) }
        }
    }

    fun deal() {
        if (_uiState.value.gameState != GameState.BETTING || _uiState.value.currentBet <= 0) return

        if (deck.needsReshuffle()) {
            deck.reshuffle()
        }

        val pHand = Hand(listOf(deck.drawCard(), deck.drawCard()))
        val dHand = Hand(listOf(deck.drawCard(), deck.drawCard()))

        _uiState.update {
            it.copy(
                gameState = GameState.PLAYER_TURN,
                playerHand = pHand,
                dealerHand = dHand,
                result = GameResult.NONE
            )
        }

        checkInitialBlackjack()
    }

    private fun checkInitialBlackjack() {
        val pBj = _uiState.value.playerHand.isBlackjack
        val dBj = _uiState.value.dealerHand.isBlackjack

        if (pBj && dBj) {
            resolveGame(GameResult.PUSH)
        } else if (pBj) {
            resolveGame(GameResult.PLAYER_BLACKJACK)
        } else if (dBj) {
            resolveGame(GameResult.DEALER_WIN)
        }
    }

    fun hit() {
        if (_uiState.value.gameState != GameState.PLAYER_TURN) return

        val newHand = _uiState.value.playerHand.addCard(deck.drawCard())
        _uiState.update { it.copy(playerHand = newHand) }

        if (newHand.isBust) {
            resolveGame(GameResult.DEALER_WIN)
        }
    }

    fun stand() {
        if (_uiState.value.gameState != GameState.PLAYER_TURN) return
        _uiState.update { it.copy(gameState = GameState.DEALER_TURN) }
        playDealerTurn()
    }

    private fun playDealerTurn() {
        var currentDealerHand = _uiState.value.dealerHand

        // Dealer stands on soft 17
        while (currentDealerHand.score() < 17) {
            currentDealerHand = currentDealerHand.addCard(deck.drawCard())
            _uiState.update { it.copy(dealerHand = currentDealerHand) }
        }

        resolveDealerEnd(currentDealerHand)
    }

    private fun resolveDealerEnd(dHand: Hand) {
        val pScore = _uiState.value.playerHand.score()
        val dScore = dHand.score()

        val result = if (dHand.isBust) {
            GameResult.PLAYER_WIN
        } else if (pScore > dScore) {
            GameResult.PLAYER_WIN
        } else if (dScore > pScore) {
            GameResult.DEALER_WIN
        } else {
            GameResult.PUSH
        }
        resolveGame(result)
    }

    private fun resolveGame(result: GameResult) {
        val bet = _uiState.value.currentBet
        var newBalance = _uiState.value.chipBalance

        when (result) {
            GameResult.PLAYER_WIN -> newBalance += (bet * 2)
            GameResult.PLAYER_BLACKJACK -> newBalance += (bet + (bet * 1.5).toInt())
            GameResult.PUSH -> newBalance += bet // Standard push: return the bet
            GameResult.DEALER_WIN -> { /* bet already deducted */ }
            GameResult.NONE -> {}
        }

        _uiState.update {
            it.copy(
                gameState = GameState.RESOLVED,
                result = result,
                chipBalance = newBalance
            )
        }
    }

    fun resetForNextHand() {
        if (_uiState.value.gameState != GameState.RESOLVED) return

        _uiState.update {
            it.copy(
                gameState = GameState.BETTING,
                playerHand = Hand(),
                dealerHand = Hand(),
                currentBet = 0,
                result = GameResult.NONE
            )
        }
    }
}
