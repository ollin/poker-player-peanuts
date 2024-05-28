package poker.player.kotlin

data class Player(
    var id: Int = 0,
    var name: String = "",
    var status: String = "",
    var version: String = "",
    var stack: Int = 0,
    var bet: Int = 0,
    var hole_cards: List<Card> = mutableListOf()
)