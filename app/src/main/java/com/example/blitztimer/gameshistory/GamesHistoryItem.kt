package com.example.blitztimer.gameshistory

import android.media.Image
import java.io.Serializable

data class GamesHistoryItem (
    var player1Name:String= "",
    var player2Name:String = "",
    var player1Result:Int = 0, //sciezka do zdjecia 1 lub 0
    var player2Result:Int = 0, //sciezka do zdjecia 1 lub 0
    var whenWasPlayed:String = "",
    var timeMillis: Long =  0,
    var gameType:String= ""):Serializable
