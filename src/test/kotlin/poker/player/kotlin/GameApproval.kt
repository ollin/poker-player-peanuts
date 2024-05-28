package poker.player.kotlin

import org.approvaltests.combinations.CombinationApprovals
import org.junit.jupiter.api.Test

class GameApproval {

    @Test
    fun testCombinations() {
        val numbers = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K")
        val suits = arrayOf("♥", "♦", "♠", "♣")
        CombinationApprovals.verifyAllCombinations({ a, b, c, d ->
            ChensAlgorithm().cardsScore(
                Card.create("$a$b"),
                Card.create("$c$d")
            )
        }, numbers, suits, numbers, suits)

//        @Test
//    fun testCombinations2() {
//        val numbers = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K")
//        val suits = arrayOf("♥", "♦", "♠", "♣")
//        CombinationApprovals.verifyAllCombinations({ a, b, c, d ->
//            val cardsScore = ChensAlgorithm().cardsScore(Card.create("$a$b"), Card.create("$c$d"))
//
//            if (cardsScore >= 5.0) return cardsScore
//            else throw
//        }, numbers, suits, numbers, suits)
//    }
    }
}