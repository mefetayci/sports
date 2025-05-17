package com.example.sports.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sports.databinding.ItemTrainingBinding
import com.example.sports.model.TrainingItem


class TrainingAdapter(private val trainingList: List<TrainingItem>) :
    RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    inner class TrainingViewHolder(val binding: ItemTrainingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val binding = ItemTrainingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = trainingList[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDate.text = item.date
    }

    override fun getItemCount(): Int = trainingList.size
}