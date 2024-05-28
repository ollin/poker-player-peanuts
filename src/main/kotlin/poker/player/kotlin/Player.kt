package poker.player.kotlin

import io.github.oshai.kotlinlogging.KotlinLogging
import org.json.JSONObject

private val logger = KotlinLogging.logger {}

private const val VERSION = "0.0.9 - pair higher"

class Game {
    fun betRequest(game_state: JSONObject): Int {
        logger.info { game_state }
        val tournament = fromJsonToTournament(game_state)
        return if (isTwoPair(tournament) || highCard(tournament)) return 3000  else 6
    }

    fun highCard(tournament: Tournament): Boolean {
        val holeCards = holeCards(tournament)
        return holeCards.get(0).rankAsInt() > 10 || holeCards.get(1).rankAsInt() > 10
    }


    private fun isTwoPair(tournament: Tournament): Boolean {
        val holeCards = holeCards(tournament)
        return holeCards!!.get(0).rank ==  holeCards!!.get(1).rank && holeCards!!.get(0).rankAsInt() > 6
    }

    private fun holeCards(tournament: Tournament): List<Card> {
        val holeCards = tournament.players.get(tournament.in_action).hole_cards
        return holeCards!!
    }

    fun showdown() {
    }

    fun version(): String {
        return VERSION
    }

    fun fromJsonToTournament(jsonObject: JSONObject): Tournament {

        val playersJson = jsonObject.getJSONArray("players")
        val players = mutableListOf<Player>()
        for (i in 0 until playersJson.length()) {
            val playerJson = playersJson.getJSONObject(i)

            val cardsJson = playerJson.optJSONArray("hole_cards")
            val cards = mutableListOf<Card>()
            if (cardsJson != null) {
                for (j in 0 until cardsJson.length()) {
                    val cardJson = cardsJson.getJSONObject(j)
                    cards.add(Card(cardJson.getString("rank"), cardJson.getString("suit")))
                }
            }

            players.add(Player(playerJson.getInt("id"),
                playerJson.getString("name"),
                playerJson.getString("status"),
                playerJson.getString("version"),
                playerJson.getInt("stack"),
                playerJson.getInt("bet"),
                cards)
            )
        }

        val communityCardsJson = jsonObject.getJSONArray("community_cards")
        val communityCards = mutableListOf<Card>()
        for (i in 0 until communityCardsJson.length()) {
            val cardJson = communityCardsJson.getJSONObject(i)
            communityCards.add(Card(cardJson.getString("rank"), cardJson.getString("suit")))
        }

        return Tournament(jsonObject.getString("tournament_id"),
            jsonObject.getString("game_id"),
            jsonObject.getInt("round"),
            jsonObject.getInt("bet_index"),
            jsonObject.getInt("small_blind"),
            jsonObject.getInt("current_buy_in"),
            jsonObject.getInt("pot"),
            jsonObject.getInt("minimum_raise"),
            jsonObject.getInt("dealer"),
            jsonObject.getInt("orbits"),
            jsonObject.getInt("in_action"),
            players,
            communityCards)
    }
}



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
)

data class Player(
    var id: Int = 0,
    var name: String = "",
    var status: String = "",
    var version: String = "",
    var stack: Int = 0,
    var bet: Int = 0,
    var hole_cards: List<Card> = mutableListOf()
)

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
            return Card(""+rank, suite)
        }
    }
}