package cm_a15316.aiapplication.domain.models

data class Card(val suit: Suit, val rank: Rank) {
    fun isAce() = rank == Rank.ACE
}
