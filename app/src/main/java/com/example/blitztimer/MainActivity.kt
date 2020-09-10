package com.example.blitztimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blitztimer.gameshistory.GamesHistoryFragment
import com.example.blitztimer.gameshistory.GamesHistoryFragment.Companion.gamesMap
import com.example.blitztimer.gameshistory.GamesHistoryItem
import com.example.blitztimer.gameshistory.GamesHistoryRecyclerViewAdapter
import com.example.blitztimer.playerslist.PlayersFragment
import com.example.blitztimer.playerslist.PlayersFragmentItem
import com.example.blitztimer.playerslist.PlayersFragmentItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.games_history_fragment.*
import kotlinx.android.synthetic.main.players_fragment.*

class MainActivity : AppCompatActivity() {
    val gamesRef = fireRef.child("games")
    val playersRef = fireRef.child("players")

    companion object {
        var currentFragment = 1
        val fireRef = FirebaseDatabase.getInstance().reference
        var listaGraczy: ArrayList<PlayersFragmentItem> = arrayListOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reloadPlayers()
        setContentView(R.layout.activity_main)
        setViewPager()
        setFragment(currentFragment)
        reloadGamesMap()
    }

    fun reloadPlayers(){
        val playersListener = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (data: DataSnapshot in p0.children) {
                    val playersItem = data.getValue(PlayersFragmentItem::class.java)!!
                    if (!listaGraczy.contains(playersItem)) {
                        listaGraczy.add(playersItem)
                    }
                }
                listOfPlayers?.adapter?.notifyDataSetChanged()
            }
        }

        playersRef.addListenerForSingleValueEvent(playersListener)
    }

    private fun reloadGamesMap() {
        val gamesListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data: DataSnapshot in p0.children) {
                    val historyItem = data.getValue(GamesHistoryItem::class.java)!!
                    gamesMap[data.key!!] = historyItem
                }
                listOfGamesHistory?.adapter?.notifyDataSetChanged()
            }
        }
        gamesRef.addListenerForSingleValueEvent(gamesListener)
    }

    override fun onRestart() {
        super.onRestart()
        reloadGamesMap()
        reloadPlayers()
        listOfPlayers?.adapter?.notifyDataSetChanged()
        listOfGamesHistory?.adapter?.notifyDataSetChanged()
        setFragment(currentFragment)
    }

    private fun setFragment(fragment: Int) {
        viewPager.setCurrentItem(fragment,false)
    }

    private fun setViewPager(){
        viewPager.adapter = getFragmentPagerAdapter()
    }

    private fun getFragmentPagerAdapter()=
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = when(position) {
                        0-> GamesHistoryFragment()
                        1-> GameChooseFragment()
                        2-> PlayersFragment()
                else->{
                    Log.wtf("problem", "problem z pagerem")
                    Fragment()
                }
            }
            override fun getCount(): Int =3
        }
    fun onBlitzClicked(view: View){
        val intent = Intent(this,ChooseTimeActivity::class.java)
        startActivity(intent)
    }
    fun onSecondPlusClicked(view: View){
        val intent = Intent(this,PlusSecondGameActivity::class.java)
        startActivity(intent)
    }
}
