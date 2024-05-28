package poker.player.kotlin

import io.github.oshai.kotlinlogging.KotlinLogging
import org.json.JSONObject

private val logger = KotlinLogging.logger {}

private const val VERSION = "0.0.21 - the start"

const val MIN_CHEN = 9.00

class Game {
    fun betRequest(game_state: JSONObject): Int {
        logger.info { game_state }
        val tournament = fromJsonToTournament(game_state)
        if (isStartingRound(tournament)) {
            return startingRound(tournament)
        }
        else {
            return 3000
        }
    }

    private fun isStartingRound(tournament: Tournament): Boolean {
        return tournament.community_cards.size == 0
    }

    private fun startingRound(tournament: Tournament): Int {
        val holeCards = holeCards(tournament)

        if (ChensAlgorithm().cardsScore(holeCards.get(0), holeCards.get(1)) >= MIN_CHEN) {
            return 3000
        } else {
            return minBet(tournament)
        }
    }

    fun highCard(tournament: Tournament): Boolean {
        val holeCards = holeCards(tournament)
        return holeCards.get(0).rankAsInt() > 10 || holeCards.get(1).rankAsInt() > 10
    }


    private fun isTwoPair(tournament: Tournament): Boolean {
        val holeCards = holeCards(tournament)
        return holeCards!!.get(0).rank ==  holeCards!!.get(1).rank && holeCards!!.get(0).rankAsInt() > minBet(tournament)
    }

    private fun minBet(tournament: Tournament): Int {
        if (inSmallBids(tournament)) {
            return tournament.current_buy_in + 2
        }
        return 0
    }

    private fun inSmallBids(tournament: Tournament): Boolean {
        val otherPlayers = extractOtherPlayers(tournament)

        return  !otherPlayers.any { p -> p.stack < p.bet }
    }

    private fun extractOtherPlayers(tournament: Tournament): List<Player> {
        return tournament.players.filter { p -> p != tournament.players.get(tournament.in_action) }
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


