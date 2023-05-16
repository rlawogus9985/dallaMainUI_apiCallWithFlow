package com.example.dallamain.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx

class Top10Decorator() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view) // 각 아이템뷰의 순서(index)
        val totalItemCount = state.itemCount // 총 아이템 수
        when (position){
            0 -> {
                outRect.left = dpToPx(16, parent.context)
                outRect.right = dpToPx(8, parent.context)
            }
            (totalItemCount - 1) -> {
                outRect.right = dpToPx(16, parent.context)
            }
            else -> {
                outRect.right = dpToPx(8, parent.context)
            }
        }
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}