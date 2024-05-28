package poker.player.kotlin

import io.kotest.core.spec.style.StringSpec


class GameTest : StringSpec({

    "dada"{
        val tournament = Tournament()
        tournament.players.get(tournament.in_action)
        tournament.players[tournament.in_action].hole_cards
        Game().highCard(tournament)
    }
})
