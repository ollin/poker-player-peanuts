package poker.player.kotlin


class ChensAlgorithm {

    private fun getRankValue(rank: String): Double {
        return when (rank) {
            "A" -> 10.0
            "K" -> 8.0
            "Q" -> 7.0
            "J" -> 6.0
            else -> rank.toDouble() / 2
        }
    }

    private fun getPairValue(rank: String): Double {
        val baseValue = getRankValue(rank)
        return if (rank == "5") baseValue * 2 + 1 else maxOf(5.0, baseValue * 2)
    }

    private fun getGapValue(rankA: String, rankB: String): Int {
        val highCard = maxOf(getRankValue(rankA), getRankValue(rankB))
        val lowCard = minOf(getRankValue(rankA), getRankValue(rankB))
        val gap = highCard - lowCard

        return when {
            gap > 4.0 || lowCard < 2 -> 5
            gap > 2.0 -> 4
            gap > 1.0 -> 2
            else -> 1
        }
    }

    fun cardsScore(card1: Card, card2: Card): Double {
        var score = 0.0

        // Check for pairs
        if (card1.rank == card2.rank) {
            score += getPairValue(card1.rank)
        } else {
            // Only consider score of the highest card
            score += maxOf(getRankValue(card1.rank), getRankValue(card2.rank))
            // Subtract for gaps
            score -= getGapValue(card1.rank, card2.rank)
            // If cards are connected or one-gap and highest card is Q or less, add point.
            if (getRankValue(card1.rank) < 7 || getRankValue(card2.rank) < 7) {
                score += 1
            }
        }
        // Add for suited case
        if (card1.suit == card2.suit) {
            score += 2
        }

        return score
    }
}
