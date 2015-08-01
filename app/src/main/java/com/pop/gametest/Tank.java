package com.pop.gametest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.pop.gametest.view.FlyView;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pengfu on 15/7/30.
 */
public class Tank implements Bullet.Callback {

    private final static int step = 5;

    private static final int DIRECT_UP    = 0 ;
    private static final int DIRECT_DOWN  = 1 ;
    private static final int DIRECT_LEFT  = 2 ;
    private static final int DIRECT_RIGHT = 3 ;
    private static final String TAG = "Tank:";
    public int left ;
    public int top ;

    public static int REGION_X;
    public static int REGION_Y;
    private int anim_index ;

    private Matrix matrix = new Matrix() ;

    private static int ANIM_SIZE = 8;

    private static int CLIP_UNIT;
    private Timer mBulletTimer ;

    private LinkedList<Bullet> mBullets ;
    public Tank() {
        CLIP_UNIT = FlyView.UNIT;
        REGION_X = (int)(FlyView.GAME_REGION_X - CLIP_UNIT*FlyView.SCALE_SIZE) ;
        REGION_Y = (int)(FlyView.GAME_REGION_Y - CLIP_UNIT*FlyView.SCALE_SIZE) ;
        mBullets = new LinkedList<Bullet>() ;
        mBulletTimer = new Timer() ;
        mBulletTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "add bullet:" + left + "X" + top);
                mBullets.addFirst(new Bullet(left, top, Tank.this));
            }
        }, 1000, 1000);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(getTankDrawable(getDirection(left, top)), left, top, new Paint());
        for (Bullet b :mBullets)
        {
            b.draw(canvas);
        }
        calStep();
    }

    private Bitmap getTankDrawable(int direction){
        Bitmap bmp = null ;
        int[] left_top = getAnimPos() ;
        matrix.reset();
        matrix.setScale(FlyView.SCALE_SIZE, FlyView.SCALE_SIZE);
        switch (direction){
            case DIRECT_UP:
                break ;
            case DIRECT_DOWN:
                matrix.postRotate(180) ;
                break ;
            case DIRECT_LEFT:
                matrix.postRotate(-90) ;
                break ;
            case DIRECT_RIGHT:
                matrix.postRotate(90) ;
                break ;
        }
        bmp = Bitmap.createBitmap(FlyView.sSheet ,left_top[0] ,left_top[1] , CLIP_UNIT, CLIP_UNIT,matrix ,false) ;
        return bmp ;
    }

    private int[] getAnimPos(){
        int[] left_top = new int[2] ;
        anim_index = anim_index % ANIM_SIZE ;
        switch (anim_index){
            case 7:
                left_top[0] = 0 ;
                left_top[1] = CLIP_UNIT ;
                break ;
            default:
                left_top[0] = (anim_index+1)*CLIP_UNIT ;
                left_top[1] = 0 ;
        }
        anim_index ++ ;
        return  left_top ;
    }

    private int getDirection(float x ,float y){
        int direct = 0 ;
        if( y == 0){
            direct = DIRECT_RIGHT ;
        }else if(x == REGION_X){
            direct = DIRECT_DOWN ;
        }else if(y == REGION_Y){
            direct = DIRECT_LEFT ;
        }else if(x == 0){
            direct = DIRECT_UP ;
        }
        return direct ;
    }

    private void calStep() {
        if (left < REGION_X && top == 0) {
            left += step;
        } else if (left == REGION_X && top < REGION_Y) {
            top += step;
        } else if (left <= REGION_X && left > 0 && top == REGION_Y) {
            left -= step;
        } else if (left == 0 && top <= REGION_Y && top > 0) {
            top -= step;
        }
        if (left > REGION_X) {
            left = REGION_X;
        }
        if (top > REGION_Y) {
            top = REGION_Y;
        }
        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
    }

    public void destroy(){
        if(mBulletTimer != null){
            mBulletTimer.cancel();
        }
    }

    @Override
    public void onOutRegion(int left, int top) {
        Log.d(TAG ,"onOutRegion:"+mBullets.size()+":left:"+left+":top:"+top) ;
        mBullets.removeLast() ;
    }
}
