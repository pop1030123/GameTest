package com.pop.gametest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by pengfu on 15/7/20.
 */
public class FlyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FlyView:";
    private SurfaceHolder holder;
    private Thread flyThread;
    private boolean flag;

    private int width;
    private int height;

    private final static int radius = 25;
    private final static int step = 10;
    private Canvas canvas;

    private int x = radius, y = radius;
    private Paint paint;

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG ,"FlyView cons.") ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        setKeepScreenOn(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG ,"surfaceCreated:"+surfaceHolder) ;
        flag = true;
        width = getMeasuredWidth() - radius;
        height = getMeasuredHeight() - radius;

        flyThread = new Thread(this);
        flyThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(TAG ,"surfaceChanged:"+surfaceHolder+":format:"+format+":width:"+width+":height:"+height) ;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG ,"surfaceDestroyed:"+surfaceHolder) ;
        flag = false;
    }

    @Override
    public void run() {

        long deltaTime =0 ;
        while (flag) {
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    long startTime = System.currentTimeMillis();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawCircle(x ,y ,radius,paint);
                    long endTime = System.currentTimeMillis();
                    deltaTime = endTime - startTime ;
                    if (deltaTime < 50) {
                        Thread.currentThread().sleep(50 - deltaTime);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "e:" + e);
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
                calStep();
            }
        }
    }

    private void calStep() {
        if (x < width && y == radius) {
            x += step;
        } else if (x == width && y < height) {
            y += step;
        } else if (x <= width && x > radius && y == height) {
            x -= step;
        } else if (x == radius && y <= height && y > radius) {
            y -= step;
        }
        if (x > width) {
            x = width;
        }
        if (y > height) {
            y = height;
        }
        if (x < radius) {
            x = radius;
        }
        if (y < radius) {
            y = radius;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
