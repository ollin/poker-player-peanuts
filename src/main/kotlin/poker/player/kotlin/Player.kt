package poker.player.kotlin

import io.github.oshai.kotlinlogging.KotlinLogging
import org.json.JSONObject

private val logger = KotlinLogging.logger {}

private const val VERSION = "0.0.8 - drain small"

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
        return holeCards!!.get(0).rank ==  holeCards!!.get(1).rank
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
    val tournament_id: String = "",
    val game_id: String= "",
    val round: Int = 0,
    val bet_index: Int = 0,
    val small_blind: Int = 0,
    val current_buy_in: Int = 0,
    val pot: Int = 0,
    val minimum_raise: Int = 0,
    val dealer: Int = 0,
    val orbits: Int = 0,
    val in_action: Int = 0,
    val players: List<Player> = emptyList(),
    val community_cards: List<Card> = emptyList()
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
}