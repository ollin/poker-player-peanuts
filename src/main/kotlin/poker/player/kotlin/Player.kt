package poker.player.kotlin

import org.json.JSONObject

class Player {
    fun betRequest(game_state: JSONObject): Int {
        return 3000
    }

    fun showdown() {
    }

    fun version(): String {
        return "0.0.3 - all in 3000"
    }
}
