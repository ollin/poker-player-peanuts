package poker.player.kotlin

import org.json.JSONObject

class Game {
    fun betRequest(game_state: JSONObject): Int {
        return 3000
    }

    fun showdown() {
    }

    fun version(): String {
        return "0.0.3 - all in 3000"
    }
}

data class Tournament(
    val tournament_id: String,
    val game_id: String,
    val round: Int,
    val bet_index: Int,
    val small_blind: Int,
    val current_buy_in: Int,
    val pot: Int,
    val minimum_raise: Int,
    val dealer: Int,
    val orbits: Int,
    val in_action: Int,
    val players: List<Player>,
    val community_cards: List<Card>
)

data class Player(
    val id: Int,
    val name: String,
    val status: String,
    val version: String,
    val stack: Int,
    val bet: Int,
    val hole_cards: List<Card>?
)

data class Card(
    val rank: String,
    val suit: String
)