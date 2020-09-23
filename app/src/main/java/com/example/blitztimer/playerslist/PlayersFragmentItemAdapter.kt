package com.example.blitztimer.playerslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blitztimer.R

class PlayersFragmentItemAdapter(val players: HashSet<PlayersFragmentItem>) : RecyclerView.Adapter<PlayersFragmentItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersFragmentItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.players_fragment_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount()= players.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlayersFragmentItemAdapter.ViewHolder, position: Int) {
        val sortedList = players.sortedWith(Comparator {o1, o2 -> if ( o1.points < o2.points) 1 else -1  })
        val currentItem = sortedList[position]
        holder.item
        holder.place.text = (position+1).toString()
        holder.playerName.text = currentItem.name
        holder.gamesPlayedByPlayer.text = "Rozegrane partie: ${currentItem.gamesPlayedByPlayer}"
        holder.points.text = "Punkty: ${currentItem.points} "
    }
    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView){
        val item: PlayersFragmentItem? = null
        val place = mView.findViewById<TextView>(R.id.placeTextView)
        val playerName = mView.findViewById<TextView>(R.id.nameTextView)
        val gamesPlayedByPlayer = mView.findViewById<TextView>(R.id.gamesPlayedByPlayerTextView)
        val points = mView.findViewById<TextView>(R.id.pointsTextView)
    }
}