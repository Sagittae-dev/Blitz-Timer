package com.example.blitztimer.playerslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blitztimer.MainActivity.Companion.listaGraczy
import com.example.blitztimer.R
import kotlinx.android.synthetic.main.players_fragment.*

class PlayersFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.players_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listOfPlayers.layoutManager = LinearLayoutManager(context)
        listOfPlayers.adapter = PlayersFragmentItemAdapter(listaGraczy)
    }
}