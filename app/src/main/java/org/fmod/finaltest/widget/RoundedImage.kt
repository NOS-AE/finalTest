package org.fmod.finaltest.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import kotlin.math.min

class RoundedImage: ImageView {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //强行把宽和高保持一致
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        if(canvas == null)
            return
        val mDrawable = drawable ?: return
        val mMatrix = imageMatrix
        if(mDrawable.intrinsicWidth == 0 || mDrawable.intrinsicHeight == 0)
            return  //宽高为0，没东西画
        if(mMatrix == null && paddingTop == 0 && paddingLeft == 0){
            mDrawable.draw(canvas)
        }else{
            val saveCount = canvas.saveCount
            canvas.save()

            if(cropToPadding){
                canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop,
                    scrollX + right - left - paddingRight,
                    scrollY + bottom - top - paddingBottom)
            }
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

            val bitmap = drawable2Bitmap(mDrawable)
            mPaint.shader = createShader(bitmap)
            canvas.drawCircle(width.toFloat() / 2,height.toFloat() / 2, width.toFloat() / 2, mPaint)
            canvas.restoreToCount(saveCount)
        }
    }

    private fun createShader(bitmap: Bitmap) = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    private fun drawable2Bitmap(drawable: Drawable): Bitmap{
        val bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val matrix = imageMatrix
        canvas.concat(matrix)
        drawable.draw(canvas)
        return bitmap
    }
}