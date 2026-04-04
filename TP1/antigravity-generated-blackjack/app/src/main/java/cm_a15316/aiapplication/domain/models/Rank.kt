package cm_a15316.aiapplication.domain.models

enum class Rank(val value: Int, val isFace: Boolean) {
    TWO(2, false), THREE(3, false), FOUR(4, false), FIVE(5, false),
    SIX(6, false), SEVEN(7, false), EIGHT(8, false), NINE(9, false),
    TEN(10, true), JACK(10, true), QUEEN(10, true), KING(10, true),
    ACE(11, false)
}
