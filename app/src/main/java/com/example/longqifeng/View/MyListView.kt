package com.example.longqifeng.View

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView


/**
 * 自定义ListView
 */
class MyListView : ListView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2,  //右移运算符，相当于除于4
            MeasureSpec.AT_MOST
        ) //测量模式取最大值
        super.onMeasure(widthMeasureSpec, heightMeasureSpec) //重新测量高度
    }
}