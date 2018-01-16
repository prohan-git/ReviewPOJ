package com.example.tjun.reviewpoj.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/1/16 0016 16:03.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写 onMeasure()，调整尺寸，让 ImageView 总是正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 先用 getMeasuredWidth() 和 getMeasuredHeight() 取到 super.onMeasure() 的计算结果
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // 然后通过计算，让宽度和高度一致
        if (width > height) {
            width = height;
        } else {
            height = width;
        }

        // 再用 setMeasuredDimension(width, height) 来保存最终的宽度和高度
        setMeasuredDimension(width, height);
    }
}
