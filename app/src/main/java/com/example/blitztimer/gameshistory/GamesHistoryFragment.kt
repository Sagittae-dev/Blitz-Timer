package com.example.blitztimer.gameshistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blitztimer.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.games_history_fragment.*

class GamesHistoryFragment: Fragment() {

companion object{
    var gamesMap: HashMap<String, GamesHistoryItem> = hashMapOf()
}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.games_history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateGamesHistoryList()
    }

    private fun updateGamesHistoryList() {
        listOfGamesHistory.layoutManager = LinearLayoutManager(context)
        listOfGamesHistory.adapter = GamesHistoryRecyclerViewAdapter(gamesMap)
        listOfGamesHistory.adapter?.notifyDataSetChanged()
    }

    /*fun updateList(){
        listOfGamesHistory?.adapter = GamesHistoryRecyclerViewAdapter(gamesMap)
    }*/
}