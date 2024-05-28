package poker.player.kotlin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class GameTest : StringSpec({

    "4 is not high card"{

        val card1 = Card.create("4♥")
        val card2 = Card.create("4♦")

        val we = Player(
            hole_cards = listOf(card1, card2)
        )
        val tournament = Tournament(
            players = listOf(we),
        )

        Game().highCard(tournament) shouldBe false
    }
})
