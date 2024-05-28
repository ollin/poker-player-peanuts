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

    "J is high card"{
        val tournament = createStartingHand("J♥", "4♦")

        Game().highCard(tournament) shouldBe true
    }

//    "beat rusty ladies" {
//        val smallBlind = 10
//
//        val card1 = Card.create("J♥")
//        val card2 = Card.create("4♦")
//        val we = Player(
//            hole_cards = listOf(card1, card2)
//        )
//
//        val card12 = Card.create("J♥")
//        val card22 = Card.create("4♦")
//        val rustyLadies = Player(
//            hole_cards = listOf(card1, card2),
//            name = "Rusty Ladies",
//            bet =
//
//
//        )
//        val tournament12 = Tournament(
//            players = listOf(we, rustyLadies),
//            small_blind = smallBlind
//        )
//
//        Game().highCard(tournament) shouldBe true
//    }


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
