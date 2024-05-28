package poker.player.kotlin

import org.json.JSONObject

class Player {
    fun betRequest(game_state: JSONObject): Int {
        return 10000
    }

    fun showdown() {
    }

    fun version(): String {
        return "0.0.2 - all in"
    }
}
