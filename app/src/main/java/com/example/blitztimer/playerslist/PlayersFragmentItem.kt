package com.example.blitztimer.playerslist

import java.io.Serializable

data class PlayersFragmentItem(
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var gamesPlayedByPlayer: Int = 0,
    var points:Int = 0,
    var place: Int = 0):Serializable