package com.example.blitztimer.gametypes

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.blitztimer.ChooseTimeActivity
import com.example.blitztimer.MainActivity.Companion.fireRef
import com.example.blitztimer.MainActivity.Companion.listaGraczy
import com.example.blitztimer.R
import com.example.blitztimer.gameshistory.GamesHistoryItem
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.games_history_fragment.*
import java.time.LocalDateTime

class GameActivity : AppCompatActivity() {

        private var gameModeIsBlitz: Boolean = true
        var timeToWhiteInMillis:Long =0
        var timeToBlackInMillis:Long=0
        private var whiteCountDownTimer: CountDownTimer? = null
        private var blackCountDownTimer: CountDownTimer? = null
        var sound: MediaPlayer?= null
        private var blackResult: Int =0
        private var whiteResult: Int =0
        private var gameIsPaused = false
        private var whiteTimerIsActiveAndBlackIsNotActive = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        sound = MediaPlayer.create(this, R.raw.finish)
        val whiteButton: Button = findViewById(R.id.whiteButton)
        val blackButton: Button = findViewById(R.id.blackButton)
        timeToWhiteInMillis = intent.getLongExtra("timeToWhite",0)*60
        timeToBlackInMillis = intent.getLongExtra("timeToBlack",0)*60
        whiteButton.text = intent.getStringExtra("whitePlayer")!!+"\n" +((timeToWhiteInMillis/1000)/60).toString()+":0"+ ((timeToWhiteInMillis/1000)%60).toString()
        blackButton.text = intent.getStringExtra("blackPlayer")!!+"\n" +((timeToBlackInMillis/1000)/60).toString()+":0"+ ((timeToBlackInMillis/1000)%60).toString()
        hideResumeAndWinsAndBackToResultButtonsAndShowPauseButton()
        gameModeIsBlitz = intent.getStringExtra("gameMode")!! == "blitz"
    }

    override fun onPause() {
        super.onPause()
        blackCountDownTimer?.cancel()
        whiteCountDownTimer?.cancel()
    }

    private fun pauseBothTimers(){
        whiteButton.isClickable = false
        blackButton.isClickable = false
        blackCountDownTimer?.cancel()
        whiteCountDownTimer?.cancel()
    }
    private fun resumeBothTimers(){
        if (whiteTimerIsActiveAndBlackIsNotActive) {
            acivateWhiteCountDownTimer()
            whiteButton.isClickable = true
        }
        else {
            activateBlackCountdownTimer()
            blackButton.isClickable = true
        }
    }

    private fun acivateWhiteCountDownTimer(){
        whiteCountDownTimer = object : CountDownTimer(timeToWhiteInMillis, 100) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinishedForWhite: Long) {
                setTextInBlackOrWhiteTimerButton(whiteButton, timeToWhiteInMillis, "whitePlayer")
                timeToWhiteInMillis = millisUntilFinishedForWhite
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                sound?.start()
                whiteButton.text = "Skończył się Twój czas"
                blackButton.text = "Białemu skończył się czas"
                finishAndSendResults(1)
            }
        }.start()
    }

    private fun activateBlackCountdownTimer(){
        blackCountDownTimer = object : CountDownTimer(timeToBlackInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinishedforblack: Long) {
                setTextInBlackOrWhiteTimerButton(blackButton, timeToBlackInMillis, "blackPlayer")
                timeToBlackInMillis = millisUntilFinishedforblack
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                sound?.start()
                whiteButton.text = "Czarnemu skończył się czas"
                blackButton.text = "Skończył się Twój czas"
                finishAndSendResults(0)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    fun setTextInBlackOrWhiteTimerButton(button : Button, timeToPlayerInMillis : Long, playersName : String){
        if(((timeToPlayerInMillis / 1000) % 60)>=10) {
        button.text = intent.getStringExtra(playersName)!!+"\n" +((timeToPlayerInMillis / 1000) / 60).toString() + ":" + ((timeToPlayerInMillis / 1000) % 60).toString()
        }
        else {
            button.text = intent.getStringExtra(playersName)!!+"\n" +((timeToPlayerInMillis / 1000) / 60).toString() + ":0" + ((timeToPlayerInMillis / 1000) % 60).toString()
        }
    }


    fun blackIsClicked(view: View){
        whiteTimerIsActiveAndBlackIsNotActive = true
        whiteButton.isClickable = true
        blackButton.isClickable = false
        blackCountDownTimer?.cancel()
        if (!gameModeIsBlitz) {
            timeToBlackInMillis += 10000
            setTextInBlackOrWhiteTimerButton(blackButton, timeToBlackInMillis, "blackPlayer")
        }
        acivateWhiteCountDownTimer()
    }

    fun whiteIsClicked(view: View){
        whiteTimerIsActiveAndBlackIsNotActive = false
        whiteButton.isClickable = false
        blackButton.isClickable = true
        whiteCountDownTimer?.cancel()
        if (!gameModeIsBlitz) {
            timeToWhiteInMillis += 10000
            setTextInBlackOrWhiteTimerButton(whiteButton, timeToWhiteInMillis, "whitePlayer")
        }
        activateBlackCountdownTimer()
    }

    private fun finishAndSendResults(wygrany: Int) { // 0- wygrał biały, 1 wygrał czarny
        pauseBothTimers()
        pauseButton.visibility = View.INVISIBLE
        backToResultsBoard.visibility = View.VISIBLE
        backToResultsBoard.isClickable = true
        for(gracze in listaGraczy){
            if(gracze.id == intent.getStringExtra("whitePlayerId")) {
                gracze.gamesPlayedByPlayer +=1
                if ( wygrany == 0) {
                    blackResult = R.drawable.numerzero
                    whiteResult = R.drawable.numerjeden
                    gracze.points += 10
                    val refToWhitePlayerId = fireRef.child("players").child(intent.getStringExtra("whitePlayerId")!!)
                    refToWhitePlayerId.child("points").setValue(gracze.points)
                    refToWhitePlayerId.child("gamesPlayedByPlayer").setValue(gracze.gamesPlayedByPlayer)
                }
            }
            if (gracze.id == intent.getStringExtra("blackPlayerId")){
                gracze.gamesPlayedByPlayer+=1
                if (wygrany == 1) {
                    blackResult = R.drawable.numerjeden
                    whiteResult = R.drawable.numerzero
                    gracze.points += 10
                    val refToBlackPlayerId = fireRef.child("players").child(intent.getStringExtra("blackPlayerId")!!)
                    refToBlackPlayerId.child("points").setValue(gracze.points)
                    refToBlackPlayerId.child("gamesPlayedByPlayer").setValue(gracze.gamesPlayedByPlayer)
                }
            }
        }
        sendToDatabase()
    }

    private fun sendToDatabase() {
        var minuteToString = LocalDateTime.now().minute.toString()
            if (LocalDateTime.now().minute<10) {
                minuteToString = "0$minuteToString"
            }
            fireRef.child("games").push().setValue(GamesHistoryItem(
            intent.getStringExtra("whitePlayer")!!,
            intent.getStringExtra("blackPlayer")!!,
            whiteResult,
            blackResult,
            "${LocalDateTime.now().year}-${LocalDateTime.now().month}-${LocalDateTime.now().dayOfMonth}  ${LocalDateTime.now().hour}:${minuteToString}",
            System.currentTimeMillis(),intent.getStringExtra("gameMode")!!
            ))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listOfGamesHistory!!.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    fun pauseButtonClicked(view: View){
            pauseBothTimers()
            gameIsPaused = true
            pauseButton.animation
            showResumeAndWinsButtonsAndHidePauseButton()
    }
    private fun hideResumeAndWinsAndBackToResultButtonsAndShowPauseButton(){
        backToResultsBoard.visibility = View.INVISIBLE
        backToResultsBoard.isClickable = false
        resumeButton.visibility = View.INVISIBLE
        resumeButton.isClickable = false
        blackWinButton.visibility = View.INVISIBLE
        blackWinButton.isClickable = false
        whiteWinButton.visibility = View.INVISIBLE
        whiteWinButton.isClickable = false
        pauseButton.isClickable = true
        pauseButton.visibility = View.VISIBLE
    }
    private fun showResumeAndWinsButtonsAndHidePauseButton(){
        resumeButton.visibility = View.VISIBLE
        resumeButton.isClickable = true
        blackWinButton.visibility = View.VISIBLE
        blackWinButton.isClickable = true
        whiteWinButton.visibility = View.VISIBLE
        whiteWinButton.isClickable = true
        pauseButton.isClickable = false
        pauseButton.visibility = View.INVISIBLE
    }

    fun resumeButtonIsClicked(view: View){
        hideResumeAndWinsAndBackToResultButtonsAndShowPauseButton()
        resumeBothTimers()
    }
    fun blackWinButtonIsClicked (view: View){
        finishAndSendResults(1)
        hideResumeAndWinsButtonsAndShowGoBackButton()
        setTextAboutWinnerOnTimerButtons(false)
    }

    fun whiteWinButtonIsClicked (view: View){
        finishAndSendResults(0)
        hideResumeAndWinsButtonsAndShowGoBackButton()
        setTextAboutWinnerOnTimerButtons(true)
    }

    private fun hideResumeAndWinsButtonsAndShowGoBackButton(){
        hideResumeAndWinsAndBackToResultButtonsAndShowPauseButton()
        backToResultsBoard.visibility = View.VISIBLE
        backToResultsBoard.isClickable = true
    }
    fun backToMainActivity(view: View){
        finish()
        ChooseTimeActivity.chooseTimeActivity?.finish()
    }
    @SuppressLint("SetTextI18n")
    fun setTextAboutWinnerOnTimerButtons(whiteWon : Boolean){ // 0 -biały wygrał, 1 - czarny wygrał
        val blackOrWhite: String = if (whiteWon) "Biały" else "Czarny"
        blackButton.text = "$blackOrWhite wygrał przed czasem."
        whiteButton.text = "$blackOrWhite wygrał przed czasem."
    }
}
