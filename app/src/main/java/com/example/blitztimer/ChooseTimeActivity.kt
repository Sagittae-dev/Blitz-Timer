package com.example.blitztimer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.blitztimer.MainActivity.Companion.listaGraczy
import com.example.blitztimer.gametypes.GameActivity
import kotlinx.android.synthetic.main.activity_choose_time.*

class ChooseTimeActivity : AppCompatActivity() {

        var gameMode : String? = null
        var timeToWhite: Int = 0
        var timeToBlack: Int = 0
        var choosenWhitePlayer : String? = null
        var choosenBlackPlayer : String? = null
        var choosenWhitePlayerId : String? = null
        var choosenBlackPlayerId : String? = null
        private var listOfPlayersNames = arrayListOf<String>()
        val arraySpinner = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        private var adapterTimes: ArrayAdapter<Int>?  = null
    companion object{
        var adapterNames: ArrayAdapter<String>? = null
        var chooseTimeActivity : ChooseTimeActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_time)
        setPlayersNamesList()
        adapterTimes = ArrayAdapter(this,android.R.layout.simple_spinner_item, arraySpinner)
        adapterNames = ArrayAdapter(this,android.R.layout.simple_spinner_item, listOfPlayersNames)
        setAdapters()
        setTimeSpinners()
        setPlayersSpinners()
        chooseTimeActivity = this
        setThisTitleAndMode()
        gameMode = intent.getStringExtra("mode")
    }

    @SuppressLint("SetTextI18n")
    private fun setThisTitleAndMode() {
        modeTitleTextView.text = "Tryb ${intent.getStringExtra("mode")}"
    }

    private fun setAdapters() {
        changeWhitePlayerSpinner.adapter = adapterNames
        changeBlackPlayerSpinner.adapter = adapterNames
        spinnerTimeForWhite.adapter = adapterTimes
        spinnerTimeForBlack.adapter = adapterTimes
    }

    private fun setPlayersNamesList() {
        for (gracze in listaGraczy) {
            listOfPlayersNames.add(gracze.name)
            Log.i("LISTA GRACZYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY", gracze.name)
        }
    }

    private fun setPlayersSpinners() {
        changeWhitePlayerSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val listOfPlayersForSpinner = listaGraczy.toList()
                choosenWhitePlayerId  = listOfPlayersForSpinner[position].id
                choosenWhitePlayer  = listOfPlayersForSpinner[position].name
                whitePlayerTextView.text = choosenWhitePlayer
                println(choosenWhitePlayerId)
                if (choosenBlackPlayer.equals(choosenWhitePlayer)){
                    startGameButton.isClickable = false
                    startGameButton.setBackgroundColor(Color.GRAY)
                }else {
                    startGameButton.isClickable = true
                    startGameButton.setBackgroundColor(Color.YELLOW)
                }
            }
        }

        changeBlackPlayerSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val listOfPlayersForSpinner = listaGraczy.toList()
                choosenBlackPlayerId  = listOfPlayersForSpinner[position].id
                choosenBlackPlayer  = listOfPlayersForSpinner[position].name
                blackPlayerTextView.text = choosenBlackPlayer
                if (choosenBlackPlayer.equals(choosenWhitePlayer)){
                    startGameButton.isClickable = false
                    startGameButton.setBackgroundColor(Color.GRAY)
                }else {
                    startGameButton.isClickable = true
                    startGameButton.setBackgroundColor(Color.YELLOW)
                }
            }
        }

    }

    private fun setTimeSpinners() {
        spinnerTimeForWhite?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeToWhite = arraySpinner[position]
                val whiteMinutesDisplay = "Dla białego $timeToWhite minut"
                forWhiteTextView.text = whiteMinutesDisplay
                println(timeToWhite)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinnerTimeForBlack?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeToBlack = arraySpinner[position]
                val blackMinutesDisplay = "Dla czarnego  $timeToBlack minut"
                forBlackTextView.text = blackMinutesDisplay
                println(timeToBlack)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    fun startGame(view: View){
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("timeToWhite", timeToWhite*1000.toLong())
        intent.putExtra("timeToBlack",timeToBlack*1000.toLong())
        intent.putExtra("blackPlayer", choosenBlackPlayer)
        intent.putExtra("whitePlayer", choosenWhitePlayer)
        intent.putExtra("blackPlayerId", choosenBlackPlayerId)
        intent.putExtra("whitePlayerId", choosenWhitePlayerId)
        intent.putExtra("gameMode", gameMode)
        startActivity(intent)
    }
    fun newPlayer(view: View){
        finish()
        val intent = Intent(this,NewPlayerActivity::class.java)
        startActivity(intent)
    }
}


