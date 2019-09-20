package com.tino.textflowlayout

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * 标签流布局
 */
class TextFlowLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private var mHorizontalSpacing: Int
    private var mVerticalSpacing: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout).apply {
            mHorizontalSpacing = getDimensionPixelSize(R.styleable.TextFlowLayout_android_horizontalSpacing, 0)
            mVerticalSpacing = getDimensionPixelSize(R.styleable.TextFlowLayout_android_verticalSpacing, 0)
        }.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var singleLineTotalWidth = 0
        var height = 0
        var lines = 1
        var maxWidth = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, child.layoutParams.width),
                getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, child.layoutParams.height)
            )
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            singleLineTotalWidth += childWidth + mHorizontalSpacing
            height = childHeight
            if (singleLineTotalWidth - mHorizontalSpacing + paddingLeft + paddingRight > MeasureSpec.getSize(
                    widthMeasureSpec
                )
            ) {
                if (singleLineTotalWidth - childWidth - mHorizontalSpacing > maxWidth) maxWidth =
                    singleLineTotalWidth - childWidth
                lines += 1
                singleLineTotalWidth = childWidth
            }
        }
        setMeasuredDimension(
            View.resolveSize(if (maxWidth > 0) maxWidth else singleLineTotalWidth, widthMeasureSpec),
            View.resolveSize(
                height,
                heightMeasureSpec
            ) * lines + (lines - 1) * mVerticalSpacing + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var totalWidth = 0
        var totalHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (l + totalWidth + childWidth + paddingLeft + paddingRight > measuredWidth) {
                totalHeight += childHeight + mVerticalSpacing
                totalWidth = 0
            }
            child.layout(
                l + totalWidth + paddingLeft,
                t + totalHeight + paddingTop,
                l + totalWidth + paddingLeft + childWidth,
                t + totalHeight + paddingTop + childHeight
            )
            totalWidth += childWidth + mHorizontalSpacing
        }
    }

    fun setData(dataList: ArrayList<String>) {
        if (childCount > 0) removeAllViews()
        for (data in dataList) {
            addView(TextView(context).apply {
                text = data
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                background = ContextCompat.getDrawable(context, R.drawable.text_view_background)
                setPadding(dip2px(5f), dip2px(5f), dip2px(5f), dip2px(5f))
            })
        }
    }

    private fun dip2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}