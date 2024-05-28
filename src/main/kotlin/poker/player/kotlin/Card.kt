package poker.player.kotlin

data class Card(
    val rank: String,
    val suit: String
) {

    fun rankAsInt(): Int {
        // convert card rank to an integer
        return when(rank.uppercase()){
            "2" -> 2
            "3" -> 3
            "4" -> 4
            "5" -> 5
            "6" -> 6
            "7" -> 7
            "8" -> 8
            "9" -> 9
            "10" -> 10
            "T" -> 10
            "J" -> 11
            "Q" -> 12
            "K" -> 13
            "A" -> 14
            else -> 0
        }

    }

    companion object {
        fun create(card: String): Card {
            val rank = card.first()
            val suite = when(card.get(1)){
                    '♥' -> "hearts"
                    '♦' -> "diamonds"
                    '♠' -> "spades"
                    else -> "clubs"

            }
            return Card("" + rank, suite)
        }
    }
}