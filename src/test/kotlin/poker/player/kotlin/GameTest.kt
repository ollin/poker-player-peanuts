package poker.player.kotlin

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class GameTest : StringSpec({

    "4 is not high card"{
        val tournament = createStartingHand("4♥", "4♦")

        Game().highCard(tournament) shouldBe false
    }

    "J is high card"{
        val tournament = createStartingHand("J♥", "4♦")

        Game().highCard(tournament) shouldBe true
    }


})

private fun createStartingHand(cardA: String, cardB: String): Tournament {
    val card1 = Card.create(cardA)
    val card2 = Card.create(cardB)

    val we = Player(
        hole_cards = listOf(card1, card2)
    )
    val tournament = Tournament(
        players = listOf(we),
    )
    return tournament
}
