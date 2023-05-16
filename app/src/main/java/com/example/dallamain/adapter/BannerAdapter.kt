package com.example.dallamain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dallamain.data.EventBannerData
import com.example.dallamain.databinding.ItemBannerBinding

class BannerAdapter(val item: ArrayList<EventBannerData>) : RecyclerView.Adapter<BannerAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(eventBannerData: EventBannerData){
            binding.eventBannerDataBinding = eventBannerData
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = item[position % item.size]
        holder.bind(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}