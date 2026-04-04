package cm_a15316.aiapplication.domain.models

class Deck(numDecks: Int = 6) {
    private val initialSize = 52 * numDecks
    var cards: MutableList<Card> = mutableListOf()
        private set

    init {
        initializeDeck(numDecks)
    }

    private fun initializeDeck(numDecks: Int) {
        cards.clear()
        for (i in 0 until numDecks) {
            for (suit in Suit.values()) {
                for (rank in Rank.values()) {
                    cards.add(Card(suit, rank))
                }
            }
        }
        cards.shuffle()
    }

    fun drawCard(): Card {
        if (cards.isEmpty()) {
            throw IllegalStateException("Deck is empty")
        }
        return cards.removeLast()
    }

    fun needsReshuffle(): Boolean {
        return cards.size < (initialSize * 0.25).toInt()
    }

    fun reshuffle(numDecks: Int = 6) {
        initializeDeck(numDecks)
    }
}
