package org.fmod.finaltest

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.util.toplevel.dp2px

private val halfDistance = dp2px(15f)
private val distance = dp2px(30f)
private val topDistance = dp2px(15f)

class BookDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if(pos and 1 == 0) {
            //左列
            outRect.set(distance, topDistance, halfDistance, 0)
        } else {
            //右列
            outRect.set(halfDistance, topDistance, distance, 0)
        }
    }
}