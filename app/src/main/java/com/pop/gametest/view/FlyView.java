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

import com.pop.gametest.R;

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

    private static int CLIP_UNIT;
    private static float SCALE_SIZE = 2.0F;
    private final static int step = 5;

    private static final int DIRECT_UP    = 0 ;
    private static final int DIRECT_DOWN  = 1 ;
    private static final int DIRECT_LEFT  = 2 ;
    private static final int DIRECT_RIGHT = 3 ;
    private Canvas canvas;

    private int x, y;
    private Paint paint;
    private Bitmap mTankBmp ;
    private Matrix matrix = new Matrix() ;

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "FlyView cons.") ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        setKeepScreenOn(true);
        matrix.setScale(SCALE_SIZE ,SCALE_SIZE);
        mTankBmp = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.tank)) ;
        CLIP_UNIT = mTankBmp.getWidth()/4 ;
        Log.d(TAG ,"tank_size:"+ CLIP_UNIT) ;
    }

    private Bitmap getTankDrawable(int direction){
        Bitmap bmp = null ;
        switch (direction){
            case DIRECT_UP:
                bmp = Bitmap.createBitmap(mTankBmp ,0 , CLIP_UNIT *3 , CLIP_UNIT, CLIP_UNIT,matrix ,false) ;
                break ;
            case DIRECT_DOWN:
                bmp = Bitmap.createBitmap(mTankBmp ,0 ,0 , CLIP_UNIT, CLIP_UNIT,matrix ,false) ;
                break ;
            case DIRECT_LEFT:
                bmp = Bitmap.createBitmap(mTankBmp ,0 , CLIP_UNIT, CLIP_UNIT, CLIP_UNIT,matrix ,false) ;
                break ;
            case DIRECT_RIGHT:
                bmp = Bitmap.createBitmap(mTankBmp ,0 , CLIP_UNIT *2 , CLIP_UNIT, CLIP_UNIT,matrix ,false) ;
                break ;
        }
        return bmp ;
    }
    private int getDirection(int x ,int y){
        int direct = 0 ;
        if( y == 0){
            direct = DIRECT_RIGHT ;
        }else if(x == width){
            direct = DIRECT_DOWN ;
        }else if(y == height){
            direct = DIRECT_LEFT ;
        }else if(x == 0){
            direct = DIRECT_UP ;
        }
        return direct ;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG ,"surfaceCreated:"+surfaceHolder) ;
        flag = true;
        width = getWidth() - (int)(CLIP_UNIT*SCALE_SIZE);
        height = getHeight() - (int)(CLIP_UNIT*SCALE_SIZE);

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
                    canvas.drawBitmap(getTankDrawable(getDirection(x ,y)), x, y, paint);
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
        if (x < width && y == 0) {
            x += step;
        } else if (x == width && y < height) {
            y += step;
        } else if (x <= width && x > 0 && y == height) {
            x -= step;
        } else if (x == 0 && y <= height && y > 0) {
            y -= step;
        }
        if (x > width) {
            x = width;
        }
        if (y > height) {
            y = height;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
