package com.example.blitztimer

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.blitztimer.MainActivity.Companion.fireRef
import com.example.blitztimer.MainActivity.Companion.listaGraczy
import com.example.blitztimer.gameshistory.GamesHistoryFragment.Companion.gamesMap
import com.example.blitztimer.gameshistory.GamesHistoryItem
import com.example.blitztimer.playerslist.PlayersFragmentItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.games_history_fragment.*
import java.time.LocalDateTime
class GameActivity : AppCompatActivity() {

        var timeToWhiteInMillis:Long =0
        var timeToBlackInMillis:Long=0
        var whiteCountDownTimer: CountDownTimer? = null
        var blackCountDownTimer: CountDownTimer? = null
        var sound: MediaPlayer?= null
        var blackResult: Int =0
        var whiteResult: Int =0
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        sound = MediaPlayer.create(this,R.raw.finish)
        val whiteButton: Button = findViewById(R.id.whiteButton)
        val blackButton: Button = findViewById(R.id.blackButton)
        //val intent : Intent = getIntent()
        timeToWhiteInMillis = intent.getLongExtra("timeToWhite",0)*60
        timeToBlackInMillis = intent.getLongExtra("timeToBlack",0)*60
        whiteButton.setText(intent.getStringExtra("whitePlayer")!!+"\n" +((timeToWhiteInMillis/1000)/60).toString()+":0"+ ((timeToWhiteInMillis/1000)%60).toString())
        blackButton.setText(intent.getStringExtra("blackPlayer")!!+"\n" +((timeToBlackInMillis/1000)/60).toString()+":0"+ ((timeToBlackInMillis/1000)%60).toString())
    }

    override fun onPause() {
        super.onPause()
        blackCountDownTimer?.cancel()
        whiteCountDownTimer?.cancel()
    }

    fun blackIsClicked(view: View){
        whiteButton.isClickable = true
        blackButton.isClickable = false
        blackCountDownTimer?.cancel()
            whiteCountDownTimer = object : CountDownTimer(timeToWhiteInMillis, 100) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    if(((timeToWhiteInMillis / 1000) % 60)>=10) {
                        whiteButton.setText(intent.getStringExtra("whitePlayer")!!+"\n" +((timeToWhiteInMillis / 1000) / 60).toString() + ":" + ((timeToWhiteInMillis / 1000) % 60).toString())
                    }
                    else {
                        whiteButton.setText(intent.getStringExtra("whitePlayer")!!+"\n" +((timeToWhiteInMillis / 1000) / 60).toString() + ":0" + ((timeToWhiteInMillis / 1000) % 60).toString())
                    }
                    timeToWhiteInMillis = millisUntilFinished
                }
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                        sound?.start()
                        whiteButton.setText("Skończył się Twój czas")
                        blackButton.setText("Białemu skończył się czas")
                        blackResult = R.drawable.numerjeden
                        whiteResult = R.drawable.numerzero
                        finishAndSendResults(1)
                }
            }.start()
    }

    fun whiteIsClicked(view: View){
        whiteButton.isClickable = false
        blackButton.isClickable = true
            whiteCountDownTimer?.cancel()
            blackCountDownTimer = object : CountDownTimer(timeToBlackInMillis, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    if(((timeToBlackInMillis / 1000) % 60)>=10) {
                        blackButton.setText(intent.getStringExtra("blackPlayer")!!+"\n" +((timeToBlackInMillis / 1000) / 60).toString() + ":" + ((timeToBlackInMillis / 1000) % 60).toString())
                    }
                    else {
                        blackButton.setText(intent.getStringExtra("blackPlayer")!!+"\n" +((timeToBlackInMillis / 1000) / 60).toString() + ":0" + ((timeToBlackInMillis / 1000) % 60).toString())
                    }
                    timeToBlackInMillis = millisUntilFinished
                }
                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    sound?.start()
                    whiteButton.setText("Czarnemu skończył się czas")
                    blackButton.setText("Skończył się Twój czas")
                    blackResult = R.drawable.numerzero
                    whiteResult = R.drawable.numerjeden
                    finishAndSendResults(0)
                }
            }.start()

    }

    private fun finishAndSendResults(wygrany: Int) { // 0- wygrał biały, 1 wygrał czarny
        whiteButton.isClickable = false
        blackButton.isClickable = false
        blackCountDownTimer?.cancel()
        whiteCountDownTimer?.cancel()

        for(gracze in listaGraczy){
            if( gracze.id.equals(intent.getStringExtra("whitePlayer"))) {
                gracze.gamesPlayedByPlayer +=1
                if ( wygrany == 0) {
                    gracze.points += 10
                    fireRef.child("players").child(intent.getStringExtra("whitePlayerId")).child("points").setValue(gracze.points)

                }
            }
            if (gracze.id.equals(intent.getStringExtra("blackPlayer"))){
                gracze.gamesPlayedByPlayer+=1
                if (wygrany == 1) {
                    gracze.points += 10
                    fireRef.child("players").child(intent.getStringExtra("blackPlayerId")).child("points").setValue(gracze.points)
                }
            }
        }
        sendToDatabase()
    }

    private fun sendToDatabase() {

            fireRef.child("games").push().setValue(GamesHistoryItem(
            intent.getStringExtra("whitePlayer"),
            intent.getStringExtra("blackPlayer"),
            whiteResult,
            blackResult,
            "${LocalDateTime.now().year}-${LocalDateTime.now().month}-${LocalDateTime.now().dayOfWeek}  ${LocalDateTime.now().hour}:${LocalDateTime.now().minute}",
            System.currentTimeMillis(),"blitz"
            ))

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        listOfGamesHistory!!.adapter!!.notifyDataSetChanged()
    }
}
