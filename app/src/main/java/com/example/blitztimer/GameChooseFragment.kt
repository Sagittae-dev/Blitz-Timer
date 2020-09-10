package com.example.blitztimer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class GameChooseFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.game_choose_fragment, container, false)
    }
    fun onBlitzClicked(view: View){
        val intent = Intent(activity,ChooseTimeActivity::class.java)
        startActivity(intent)
    }
    fun onSecondPlusClicked(view: View){
        val intent = Intent(activity,PlusSecondGameActivity::class.java)
        startActivity(intent)
    }
}