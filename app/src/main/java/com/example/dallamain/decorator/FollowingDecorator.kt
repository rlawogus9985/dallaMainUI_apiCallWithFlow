package com.example.dallamain.decorator

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FollowingDecorator : RecyclerView.ItemDecoration() {

    private val VIEW_TYPE_FOLLOWING = 0
    private val VIEW_TYPE_ETC = 1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view) // 각 아이템뷰의 순서(index)
        val totalItemCount = state.itemCount // 총 아이템 수
        Log.d("position","${position}")
        if(parent.adapter?.getItemViewType(position) == VIEW_TYPE_FOLLOWING ){
            when(position){
                0 -> {
                    outRect.left = dpToPx(16, view)
                    outRect.right = dpToPx(10, view)
                }
                (totalItemCount - 1) -> {
                    outRect.right = dpToPx(4, view)
                }
                else -> {
                    outRect.right = dpToPx(10, view)
                }
            }
        } else if(parent.adapter?.getItemViewType(position) == VIEW_TYPE_ETC) {
            outRect.top = dpToPx(5, view)
            outRect.right = dpToPx(4, view)
        }
    }

    private fun dpToPx(dp: Int, view: View): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),view.resources.displayMetrics).toInt()
    }
}