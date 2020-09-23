package com.example.blitztimer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.blitztimer.ChooseTimeActivity.Companion.adapterNames
import com.example.blitztimer.MainActivity.Companion.currentFragment
import com.example.blitztimer.MainActivity.Companion.fireRef
import com.example.blitztimer.playerslist.PlayersFragmentItem
import kotlinx.android.synthetic.main.activity_new_player.*
import java.util.*

class NewPlayerActivity : AppCompatActivity() {
    var id = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_player)
    }

    fun sendNewPlayerToDatabase(view: View){
        fireRef.child("players").child(id).setValue(PlayersFragmentItem(id,nameText.text.toString(),surnameText.text.toString()))
        adapterNames?.notifyDataSetChanged()
        currentFragment = 2
        finish()
    }

    fun hideKeyb(view: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}
