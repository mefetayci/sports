package com.example.sports.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.databinding.ItemStatsBinding
import com.example.sports.model.StatsItem


class StatsAdapter(private val statsList: List<StatsItem>) :
    RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    inner class StatsViewHolder(val binding: ItemStatsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val binding = ItemStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val item = statsList[position]
        holder.binding.tvPlayerName.text = item.playerName
        holder.binding.tvGoals.text = "Goals: ${item.goals}"
        holder.binding.tvAssists.text = "Assists: ${item.assists}"
    }

    override fun getItemCount(): Int = statsList.size
}