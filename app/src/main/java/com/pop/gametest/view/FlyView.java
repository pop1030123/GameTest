package com.pop.gametest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pop.gametest.App;
import com.pop.gametest.R;
import com.pop.gametest.Tank;

import java.util.ArrayList;
import java.util.List;

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

    private Canvas canvas;
    private Paint paint;

    private List<Tank> tanks ;

    private Bitmap mTile ;
    public static Bitmap sSheet ;
    public static int UNIT ;

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "FlyView cons.") ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        sSheet = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.tanks_sheet)) ;
        UNIT = sSheet.getWidth() / 8 ;
        mTile = Bitmap.createBitmap(sSheet,0,0,UNIT ,UNIT) ;
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG ,"surfaceCreated:"+surfaceHolder) ;
        flag = true;
        width = getWidth();
        height = getHeight();
        tanks = new ArrayList<Tank>() ;
        tanks.add(new Tank(width ,height)) ;
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
                    int left = 0 ;
                    int top = 0 ;
                    while (left < width){
                        while (top < height){
                            canvas.drawBitmap(mTile ,left ,top ,paint);
                            top += UNIT ;
                        }
                        top = 0 ;
                        left += UNIT ;
                    }

                    for (Tank t : tanks){
                        t.draw(canvas);
                    }
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
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG ,"onTouchEvent:"+event.getAction()) ;
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(tanks.size() < 4){
                    tanks.add(new Tank(width ,height)) ;
                }
                break ;
        }
        return true;
    }
}
