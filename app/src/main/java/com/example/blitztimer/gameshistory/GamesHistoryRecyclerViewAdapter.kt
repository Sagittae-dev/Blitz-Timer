package com.example.blitztimer.gameshistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blitztimer.R

class GamesHistoryRecyclerViewAdapter(val games: HashMap<String, GamesHistoryItem>) :RecyclerView.Adapter<GamesHistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.games_history_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount()= games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gamesSortedList = games.toList().sortedWith(Comparator { o1, o2 -> if ( o1.second.timeMillis < o2.second.timeMillis) 1 else -1  })
        val currentitem = gamesSortedList[position].second
        holder.item
        holder.player1Name?.text =currentitem.player1Name
        holder.player2Name?.text =currentitem.player2Name
        holder.player1Result?.setImageResource(currentitem.player1Result)
        holder.player2Result?.setImageResource(currentitem.player2Result)
        holder.gameType.text = currentitem.gameType
        holder.whenWasPlayed.text = currentitem.whenWasPlayed.toString()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView){
        val item: GamesHistoryItem? = null
        val player1Name =mView.findViewById<TextView>(R.id.player1TextView)
        val player2Name =mView.findViewById<TextView>(R.id.player2TextView)
        val player1Result =mView.findViewById<ImageView>(R.id.player1ResultImageView)
        val player2Result =mView.findViewById<ImageView>(R.id.player2ResultImageView)
        val whenWasPlayed =mView.findViewById<TextView>(R.id.whenWasPlayedTextView)
        val gameType = mView.findViewById<TextView>(R.id.gameTypeTextView)
    }
}