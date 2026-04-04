package cm_a15316.aiapplication.domain

import cm_a15316.aiapplication.domain.models.Card
import cm_a15316.aiapplication.domain.models.Deck
import cm_a15316.aiapplication.domain.models.Rank
import cm_a15316.aiapplication.domain.models.Suit
import org.junit.Assert.assertEquals
import org.junit.Test

class GameEngineTest {

    private fun createDeckWithCards(vararg cards: Card): Deck {
        val deck = Deck(1)
        deck.cards.clear()
        deck.cards.addAll(cards.toList().reversed())
        return deck
    }

    @Test
    fun `dealer stands on soft 17`() {
        val deck = createDeckWithCards(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.ACE), Card(Suit.CLUBS, Rank.SIX),
            Card(Suit.SPADES, Rank.TWO)
        )
        val engine = GameEngine(deck)
        engine.syncBalance(100)
        engine.placeBet(10)
        engine.deal()
        engine.stand()

        assertEquals(GameState.RESOLVED, engine.uiState.value.gameState)
        assertEquals(17, engine.uiState.value.dealerHand.score())
        assertEquals(20, engine.uiState.value.playerHand.score())
        assertEquals(GameResult.PLAYER_WIN, engine.uiState.value.result)
    }

    @Test
    fun `standard push returns bet and ends hand`() {
        val deck = createDeckWithCards(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.TEN), Card(Suit.CLUBS, Rank.TEN)
        )
        val engine = GameEngine(deck)
        engine.syncBalance(100)
        engine.placeBet(10)
        engine.deal()
        engine.stand()

        assertEquals(GameState.RESOLVED, engine.uiState.value.gameState)
        assertEquals(GameResult.PUSH, engine.uiState.value.result)
        assertEquals(100, engine.uiState.value.chipBalance)
    }

    @Test
    fun `dealer hits on hard 16`() {
        val deck = createDeckWithCards(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.HEARTS, Rank.NINE),
            Card(Suit.DIAMONDS, Rank.TEN), Card(Suit.CLUBS, Rank.SIX),
            Card(Suit.SPADES, Rank.FIVE)
        )
        val engine = GameEngine(deck)
        engine.syncBalance(100)
        engine.placeBet(10)
        engine.deal()
        engine.stand()

        assertEquals(GameState.RESOLVED, engine.uiState.value.gameState)
        assertEquals(21, engine.uiState.value.dealerHand.score())
        assertEquals(GameResult.DEALER_WIN, engine.uiState.value.result)
    }

    @Test
    fun `player blackjack pays 3 to 2`() {
        val deck = createDeckWithCards(
            Card(Suit.SPADES, Rank.ACE), Card(Suit.HEARTS, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.TWO), Card(Suit.CLUBS, Rank.THREE)
        )
        val engine = GameEngine(deck)
        engine.syncBalance(100)
        engine.placeBet(10)
        engine.deal()

        assertEquals(GameState.RESOLVED, engine.uiState.value.gameState)
        assertEquals(GameResult.PLAYER_BLACKJACK, engine.uiState.value.result)
        assertEquals(115, engine.uiState.value.chipBalance)
    }

    @Test
    fun `player bust ends game immediately`() {
        val deck = createDeckWithCards(
            Card(Suit.SPADES, Rank.TEN), Card(Suit.SPADES, Rank.SIX),
            Card(Suit.DIAMONDS, Rank.TWO), Card(Suit.CLUBS, Rank.THREE),
            Card(Suit.HEARTS, Rank.SIX)
        )
        val engine = GameEngine(deck)
        engine.syncBalance(100)
        engine.placeBet(10)
        engine.deal()
        engine.hit()

        assertEquals(GameState.RESOLVED, engine.uiState.value.gameState)
        assertEquals(GameResult.DEALER_WIN, engine.uiState.value.result)
    }
}
