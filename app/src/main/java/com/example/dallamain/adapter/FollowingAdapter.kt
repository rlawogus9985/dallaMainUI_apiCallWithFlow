package com.example.dallamain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dallamain.R
import com.example.dallamain.data.FollowingData
import com.example.dallamain.databinding.ItemFollowingBinding
import com.example.dallamain.databinding.ItemFollowingEtcBinding

class FollowingAdapter(private val item: ArrayList<FollowingData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_FOLLOWING = 0
    private val VIEW_TYPE_ETC = 1
    private val ETC_POSITION = 7

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FOLLOWING -> {
                val binding = ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FollowingViewHolder(binding)
            }
            else -> {
                val binding = ItemFollowingEtcBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EtcViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (item.size > ETC_POSITION) (ETC_POSITION + 1) else item.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == ETC_POSITION){
            VIEW_TYPE_ETC
        } else {
            VIEW_TYPE_FOLLOWING
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = item[position]

        if(getItemViewType(position) == VIEW_TYPE_FOLLOWING){
            val holder = holder as FollowingViewHolder
            holder.followingBind(currentItem)

        } else if(getItemViewType(position) == VIEW_TYPE_ETC){
            val holder = holder as EtcViewHolder
            holder.etcText.text = holder.itemView.context.getString(R.string.etc_count, item.size - ETC_POSITION)
        }

    }

    class FollowingViewHolder(private val binding: ItemFollowingBinding) : RecyclerView.ViewHolder(binding.root){
        fun followingBind(followingData: FollowingData){
            binding.followingProfileImage.clipToOutline = true
            binding.followingDataBinding = followingData
        }
    }
    class EtcViewHolder(private val binding: ItemFollowingEtcBinding): RecyclerView.ViewHolder(binding.root){
        val etcText = binding.etcText
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}