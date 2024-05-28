package poker.player.kotlin

data class Tournament(
    var tournament_id: String = "",
    var game_id: String= "",
    var round: Int = 0,
    var bet_index: Int = 0,
    var small_blind: Int = 0,
    var current_buy_in: Int = 0,
    var pot: Int = 0,
    var minimum_raise: Int = 0,
    var dealer: Int = 0,
    var orbits: Int = 0,
    var in_action: Int = 0,
    var players: List<Player> = mutableListOf(),
    var community_cards: List<Card> = emptyList()

) {
    fun us(): Player {
        return players.get(in_action)
    }
}
