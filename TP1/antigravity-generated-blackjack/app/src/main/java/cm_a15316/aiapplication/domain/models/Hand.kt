package cm_a15316.aiapplication.domain.models

data class Hand(val cards: List<Card> = emptyList()) {
    fun addCard(card: Card): Hand {
        return copy(cards = cards + card)
    }

    fun score(): Int {
        var score = cards.sumOf { it.rank.value }
        var aces = cards.count { it.isAce() }

        while (score > 21 && aces > 0) {
            score -= 10
            aces -= 1
        }
        return score
    }

    val isBust: Boolean get() = score() > 21
    val isBlackjack: Boolean get() = cards.size == 2 && score() == 21
    val isSoft17: Boolean get() {
        if (score() != 17) return false
        val baseScore = cards.sumOf { if (it.isAce()) 1 else it.rank.value }
        return baseScore <= 7 && cards.any { it.isAce() }
    }
}
