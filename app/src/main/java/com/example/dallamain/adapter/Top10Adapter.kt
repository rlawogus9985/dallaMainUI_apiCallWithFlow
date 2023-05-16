package com.example.dallamain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dallamain.data.Top10Data
import com.example.dallamain.databinding.ItemTop10Binding

class Top10Adapter(private val item: ArrayList<Top10Data>)
    : RecyclerView.Adapter<Top10Adapter.CustomViewHolder>(){

    class CustomViewHolder(private val binding: ItemTop10Binding): RecyclerView.ViewHolder(binding.root){
        fun bind(top10Data: Top10Data){
            binding.top10ProfileImage.clipToOutline = true
            binding.top10DataBinding = top10Data
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemTop10Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}