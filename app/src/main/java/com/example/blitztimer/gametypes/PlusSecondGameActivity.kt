package com.example.blitztimer.gametypes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.blitztimer.R
import kotlinx.android.synthetic.main.activity_plus_second_game.*

class PlusSecondGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_second_game)

        whiteButton.text = SystemClock.elapsedRealtime().toString()
    }
}

