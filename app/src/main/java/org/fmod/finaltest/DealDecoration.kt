package org.fmod.finaltest

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import org.fmod.finaltest.bean.MainListGroupInfo

class DealDecoration(
    private val mBottomOffset: Int,
    private val mHeaderHeight: Int,
    private val mHeaderTextSize: Float,
    private val callback: Callback
): RecyclerView.ItemDecoration() {

    companion object{
        @ColorInt
        const val STICKY_HEADER_TEXT_COLOR = (0xFF6D6B6B).toInt()
    }

    private val mFontMetrics: Paint.FontMetrics

    private val mTextPaint = Paint().apply {
        color = STICKY_HEADER_TEXT_COLOR
        textSize = mHeaderTextSize
        mFontMetrics = fontMetrics
    }

    private val mPaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        /*
            添加空白到最后一个item的底部
            以避免最后一个item被bottomBar覆盖
         */
        val itemCount = state.itemCount
        val pos = parent.getChildAdapterPosition(view)
        if(pos == itemCount - 1) outRect.bottom = mBottomOffset

        /*if(pos > 0){
            val groupInfo = callback.getGroupInfo(pos - 1)
            outRect.top = if(groupInfo.isFirstInGroup()) mHeaderHeight else 0
        }*/
    }

    /*override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val chileCount = parent.childCount
        var view: View
        var index: Int
        var groupInfo: MainListGroupInfo

        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        var suggestTop: Int

        //遍历屏幕上可见的item
        for(i in 0 until chileCount){
            view = parent.getChildAt(i)
            index = parent.getChildAdapterPosition(view)

            //第一个Item是AllStatistics
            if(index == 0)
                continue

            groupInfo = callback.getGroupInfo(index)
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight

            if(i != 0){ //非第一个可见item
                //只有组内第一个Item才绘制

                if(groupInfo.isFirstInGroup()){
                    top = view.top - mHeaderHeight
                    bottom = view.top
                    drawHeader(c,groupInfo, left,right,top,bottom)
                }

            }else{  //第一个可见item
                //无论是不是组内第一个Item，都要绘制对应组的Header

                top = parent.paddingTop

                if(groupInfo.isLastInGroup()){
                    suggestTop = view.bottom - mHeaderHeight
                    if(suggestTop < top){
                        top = suggestTop
                    }
                }
                bottom = top + mHeaderHeight

                drawHeader(c, groupInfo, left, right, top, bottom)
            }
        }
    }

    private fun drawHeader(c: Canvas, info: MainListGroupInfo, left: Int, right: Int, top: Int, bottom: Int){
        //绘制Header
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)

        val titleX = left
        val titleY = bottom - mFontMetrics.descent
        //绘制title
        c.drawText(info.title, titleX.toFloat(),titleY,mTextPaint)
    }*/

    interface Callback{
        /**
         * @param pos GroupInfo数组的下标
         */
        fun getGroupInfo(pos: Int): MainListGroupInfo
    }
}