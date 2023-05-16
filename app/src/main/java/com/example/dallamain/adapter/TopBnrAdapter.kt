package com.example.dallamain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dallamain.R
import com.example.dallamain.data.TopBnrData
import com.example.dallamain.databinding.ItemTopbnrBinding

class TopBnrAdapter(private val item: ArrayList<TopBnrData>)
    : RecyclerView.Adapter<TopBnrAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemTopbnrBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.tag = binding // 뷰에 바인딩 객체를 설정.

        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(item[position % item.size])
    }

    class CustomViewHolder(private val binding: ItemTopbnrBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(topBnrData: TopBnrData){
            binding.topBnrDataBinding = topBnrData
        }
    }
}