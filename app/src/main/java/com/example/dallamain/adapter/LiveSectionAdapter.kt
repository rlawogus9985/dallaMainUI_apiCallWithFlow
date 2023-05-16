package com.example.dallamain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dallamain.R
import com.example.dallamain.data.LiveSectionData
import com.example.dallamain.databinding.ItemLivesectionBinding

class LiveSectionAdapter(val item: ArrayList<LiveSectionData>) :
    RecyclerView.Adapter<LiveSectionAdapter.CustomViewHolder>(){

    class CustomViewHolder(private val binding: ItemLivesectionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(liveSectionData: LiveSectionData){
            binding.profileImage.clipToOutline = true
            binding.liveSectionDataBinding = liveSectionData
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = item[position]
        holder.bind(currentItem)

//        // 배지1
//        if (currentItem.bdgImage1 == null){
//            val params = holder.liveTitleText.layoutParams as ConstraintLayout.LayoutParams
//            params.topToTop = holder.profileImage.id
//            params.topMargin = dpToPx(9, holder.itemView.context.applicationContext)
//        } else {
//            holder.bdgImage1.setImageResource(currentItem.bdgImage1)
//        }
//        // 배지2
//        if (currentItem.bdgImage2 != null){
//            holder.bdgImage2.setImageResource(currentItem.bdgImage2)
//        }
//
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemLivesectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

}