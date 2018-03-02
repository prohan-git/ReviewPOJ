package com.example.tjun.reviewpoj.ui.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/1/26 0026 12:09.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class SurfaceViewL extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private boolean isDrawing;
    private Canvas mCanvas;

    public SurfaceViewL(Context context) {
        this(context, null);
    }

    public SurfaceViewL(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSurfaceView();
    }

    private void initSurfaceView() {
        mSurfaceHolder = getHolder();//得到SurfaceHolder对象
        mSurfaceHolder.addCallback(this);//注册SurfaceHolder
        setFocusable(true);//能否获得焦点
        setFocusableInTouchMode(true);//能否通过触摸获取焦点
        this.setKeepScreenOn(true);//保持屏幕长亮

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    @Override
    public void run() {
        while (isDrawing) {
            drawing();
        }
    }

    private void drawing() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();


        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

}
