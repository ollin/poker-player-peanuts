package poker.player.kotlin

import io.github.oshai.kotlinlogging.KotlinLogging
import org.json.JSONObject

private val logger = KotlinLogging.logger {}

private const val VERSION = "0.0.26 - refactor"

const val MIN_CHEN = 9.00

 const val RUSTY_LADIES = "Rusty Ladies"
 const val POKER_PRESTIDIGITATORS = "Poker Prestidigitators"

class Game {
    fun betRequest(game_state: JSONObject): Int {
        logger.info { game_state }
        val tournament = fromJsonToTournament(game_state)
        if (isStartingRound(tournament)) {
            if (isOnlyLars(tournament) && tournament.us().bet > 300) {
                return 3000
            }
            return startingRound(tournament)
        }
        else {
            return withCommunityCards(tournament)
        }
    }

    private fun isStartingRound(tournament: Tournament): Boolean {
        return tournament.community_cards.size == 0
    }

    private fun isOnlyLars(tournament: Tournament): Boolean {
        return tournament.players.first { p -> p.name == RUSTY_LADIES }.status == "folded"
    }
    private fun isOnlyLadies(tournament: Tournament): Boolean {
        return tournament.players.first { p -> p.name == POKER_PRESTIDIGITATORS }.status == "folded"
    }

    private fun startingRound(tournament: Tournament): Int {
        val holeCards = holeCards(tournament)

        val chenScore = ChensAlgorithm().cardsScore(holeCards.get(0), holeCards.get(1))

        if (chenScore >= MIN_CHEN) {
            return 3000
        }
        if (chenScore >= 6.00 && tournament.pot < 200) {
            return 500
        }
        else {
            return minBet(tournament)
        }
    }

    private fun withCommunityCards(tournament: Tournament): Int {
        val allCards = tournament.community_cards.toMutableList()
        allCards.addAll(ourCards(tournament))

        if (hasPair(allCards) || has4Suited(allCards) || hasStraight(allCards)) {
            return 3000
        } else {
            return 0
        }
    }

    fun hasStraight(allCards: List<Card>): Boolean {
        val ranks = allCards.map { it.rankAsInt() }.sorted()

        for (i in 0 until ranks.size - 4) {
            if (ranks.subList(i, i + 5).zipWithNext().all { (a, b) -> b - a == 1 }) {
                return true
            }
        }

        // Check Ace-low (A,2,3,4,5) and Ace-high (10,J,Q,K,A) straights
        val aceLowRanks = listOf(14,2,3,4,5)
        val aceHighRanks = listOf(10,11,12,13,14)

        if(ranks.containsAll(aceLowRanks) || ranks.containsAll(aceHighRanks)){
            return true
        }

        return false
    }

    private fun has4Suited(allCards: MutableList<Card>): Boolean {
        return allCards.groupingBy { card -> card.suit }.eachCount().any {group -> group.value >= 4}
    }

    private fun hasPair(allCards: MutableList<Card>): Boolean {
        // find pair
        return allCards.any { card -> allCards.count { it.rank == card.rank } >= 2 }
    }


    private fun ourCards(tournament: Tournament): List<Card> {
        return tournament.players.get(tournament.in_action).hole_cards

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


