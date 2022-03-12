package com.androidapp.myfootballcoach.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.myfootballcoach.databinding.ItemPlayerBinding
import com.androidapp.myfootballcoach.domain.Player
import com.bumptech.glide.Glide

class PlayersAdapter(val context: Context, private val onClick: (Player) -> Unit) :
    RecyclerView.Adapter<PlayerViewHolder>() {

    private var playersList: List<Player> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playersList.get(position), onClick, context)
    }

    override fun getItemCount(): Int {
        return playersList.size
    }

    fun setPlayersList(items: ArrayList<Player>) {
        playersList = items
        notifyDataSetChanged()
    }

}

/**
 * ViewHolder is a single element of the list.
 * For each element of the list a Viewholder is created.
 */
class PlayerViewHolder(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(player: Player, onClick: (Player) -> Unit, context: Context) {
        Glide.with(context).load(player.picture.thumbnail).into(binding.playerThumbnail)
        binding.playerFullName.text = player.name.title + " " + player.name.first + " " + player.name.last
        binding.root.setOnClickListener {
            onClick(player)
        }
    }

}
