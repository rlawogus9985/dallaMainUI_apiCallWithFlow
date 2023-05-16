package com.example.dallamain.bindingadapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.dallamain.R
import com.google.android.material.internal.ViewUtils.dpToPx

object BindingAdapters {

    @BindingAdapter("bindImageToViewWithUrl")
    @JvmStatic
    fun setImage(imageView: ImageView, imageUrl: String?){
        if(imageUrl != null){
            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)
        }
    }

    @BindingAdapter("topBnrBadge")
    @JvmStatic
    fun setTopBnrBadge(imageView: ImageView, number: Int){
        if(number == 1){
            Glide.with(imageView.context)
                .load(R.drawable.bdg_star)
                .into(imageView)
        }
    }

    @BindingAdapter("bindImageToViewWithDrawableId")
    @JvmStatic
    fun setRankingImage(imageView: ImageView, number: Int){
        Glide.with(imageView.context)
            .load(number)
            .into(imageView)
    }

    @BindingAdapter("followingFirstBackground")
    @JvmStatic
    fun setFollowingFirstBackgroundImage(imageView: ImageView, isBroadCasting: String?){
        if(isBroadCasting == "y"){
            Glide.with(imageView.context).load(R.drawable.gradient_pink_circle_background).into(imageView)
        } else if(isBroadCasting == "n"){
            Glide.with(imageView.context).load(R.drawable.gradient_gray_circle_background).into(imageView)
        }
    }

    @BindingAdapter("liveSectionGenderImage")
    @JvmStatic
    fun setLiveSectionGenderImage(imageView: ImageView, gender: String?){
        if(gender == "m"){
            Glide.with(imageView.context).load(R.drawable.ico_male).into(imageView)
        } else if(gender =="f"){
            Glide.with(imageView.context).load(R.drawable.ico_female).into(imageView)
        }
    }

    @BindingAdapter("liveSectionLikeImage")
    @JvmStatic
    fun setLiveSectionLikeImage(imageView: ImageView, rising: String?){
        if(rising == "y"){
            Glide.with(imageView.context).load(R.drawable.ico_booster_2).into(imageView)
        } else {
            Glide.with(imageView.context).load(R.drawable.heart).into(imageView)
        }
    }

    @BindingAdapter("liveSectionLikeColor")
    @JvmStatic
    fun setLiveSectionLikeColor(view: TextView, rising: String?){
        if(rising == "y"){
            view.setTextColor(ContextCompat.getColor(view.context,R.color.pink))
        } else {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.gray_99))
        }
    }

    @BindingAdapter("liveSectionBdgImage")
    @JvmStatic
    fun setLiveSectionBdgImage(imageView: ImageView, image: Int?){
        if(image != null){
            Glide.with(imageView.context)
                .load(image)
                .into(imageView)
        } else {
            imageView.visibility = View.GONE
        }
    }

    @BindingAdapter("liveSectionTitleLocation")
    @JvmStatic
    fun setLiveSectionTitleLocation(view: TextView, image: Int?){
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        if(image == null){
            params.topMargin = dpToPx(9, view.context.applicationContext)
        }
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}