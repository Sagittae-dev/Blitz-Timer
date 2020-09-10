package com.example.blitztimer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.blitztimer.MainActivity.Companion.listaGraczy
import com.example.blitztimer.playerslist.PlayersFragment
import com.example.blitztimer.playerslist.PlayersFragmentItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_choose_time.*
import kotlinx.android.synthetic.main.activity_new_player.*
import java.util.*

class ChooseTimeActivity : AppCompatActivity() {

        var timeToWhite: Int = 0
        var timeToBlack: Int = 0
        var choosenWhitePlayer : String? = null
        var choosenBlackPlayer : String? = null
        var choosenWhitePlayerId : String? = null
        var choosenBlackPlayerId : String? = null
        var listOfPlayersNames = arrayListOf<String>()
        val arraySpinner = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        var adapterTimes: ArrayAdapter<Int>?  = null
    companion object{
        var adapterNames: ArrayAdapter<String>? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_time)
        setPlayersNamesList()
        adapterTimes = ArrayAdapter(this,android.R.layout.simple_spinner_item, arraySpinner)


        adapterNames = ArrayAdapter(this,android.R.layout.simple_spinner_item,listOfPlayersNames)
        setAdapters()
        setTimeSpinners()
        setPlayersSpinners()
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
                choosenWhitePlayerId  = listaGraczy.get(position).id
                choosenWhitePlayer  = listaGraczy.get(position).name
                whitePlayerTextView.text = choosenWhitePlayer
            }
        }

        changeBlackPlayerSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                choosenBlackPlayerId  = listaGraczy.get(position).id
                choosenBlackPlayer  = listaGraczy.get(position).name
                blackPlayerTextView.text = choosenBlackPlayer
            }
        }

    }

    private fun setTimeSpinners() {
        spinnerTimeForWhite?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeToWhite = arraySpinner.get(position)
                val whiteMinutesDisplay = "Dla białego" +" "+ timeToWhite +" minut"
                forWhiteTextView.text = whiteMinutesDisplay
                println(timeToWhite)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinnerTimeForBlack?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeToBlack = arraySpinner.get(position)
                val blackMinutesDisplay = "Dla czarnego " +" "+ timeToBlack +" minut"
                forBlackTextView.text = blackMinutesDisplay
                println(timeToBlack)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    fun startGame(view: View){
        var intent = Intent(this, GameActivity::class.java)
        intent.putExtra("timeToWhite", timeToWhite*1000.toLong())
        intent.putExtra("timeToBlack",timeToBlack*1000.toLong())
        intent.putExtra("blackPlayer", choosenBlackPlayer)
        intent.putExtra("whitePlayer", choosenWhitePlayer)
        intent.putExtra("blackPlayerId", choosenBlackPlayerId)
        intent.putExtra("whitePlayerId", choosenWhitePlayerId)
        startActivity(intent)
    }
    fun newPlayer(view: View){
        finish()
        val intent = Intent(this,NewPlayerActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        //setAdapters()
    }
}


