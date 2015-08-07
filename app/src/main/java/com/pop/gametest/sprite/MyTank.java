package com.pop.gametest.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.pop.gametest.Const;
import com.pop.gametest.view.FlyView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pengfu on 15/7/30.
 */
public class MyTank implements Sprite {

    private final static int step = 5;

    private static final String TAG = "MyTank:";
    public int left ;
    public int top ;
    private int direction = Const.DIRECT_UP ;

    public static int REGION_X;
    public static int REGION_Y;
    private int anim_index ;

    public static final int STATUS_UNKNOWN = -1 ;
    public static final int STATUS_RUNNING  = 0 ;
    private int status = STATUS_UNKNOWN ;

    private Matrix matrix = new Matrix() ;

    private static int ANIM_SIZE = 8;

    private Timer mBulletTimer ;

    public MyTank() {
        REGION_X = (int)(FlyView.GAME_REGION_X - FlyView.SCALED_UNIT) ;
        REGION_Y = (int)(FlyView.GAME_REGION_Y - FlyView.SCALED_UNIT) ;
        left = REGION_X /2 ;
        top  = REGION_Y /2 ;
        mBulletTimer = new Timer() ;
        mBulletTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SpriteManager.getInstance().getBullets().add(new Bullet(left, top, direction, SpriteManager.getInstance()));
            }
        }, 1000, 1000);
    }

    public void draw(Canvas canvas ,int direction){
        this.direction = direction ;
        canvas.drawBitmap(getTankDrawable(), left, top, new Paint());
    }

    public void setStatus(int status){
        this.status = status ;
    }
    private Bitmap getTankDrawable(){
        Bitmap bmp = null ;
        int[] left_top = getAnimPos() ;
        matrix.reset();
        matrix.setScale(FlyView.SCALE_SIZE, FlyView.SCALE_SIZE);
        switch (direction){
            case Const.DIRECT_UP:
                break ;
            case Const.DIRECT_DOWN:
                matrix.postRotate(180) ;
                break ;
            case Const.DIRECT_LEFT:
                matrix.postRotate(-90) ;
                break ;
            case Const.DIRECT_RIGHT:
                matrix.postRotate(90) ;
                break ;
        }
        bmp = Bitmap.createBitmap(FlyView.sSheet ,left_top[0] ,left_top[1] , FlyView.UNIT, FlyView.UNIT,matrix ,false) ;
        return bmp ;
    }

    private int[] getAnimPos(){
        int[] left_top = new int[2] ;
        anim_index = anim_index % ANIM_SIZE ;
        switch (anim_index){
            case 7:
                left_top[0] = 0 ;
                left_top[1] = FlyView.UNIT*2 ;
                break ;
            default:
                left_top[0] = (anim_index+1)*FlyView.UNIT ;
                left_top[1] = FlyView.UNIT ;
        }
        if(status == STATUS_RUNNING){
            anim_index ++ ;
        }
        return  left_top ;
    }

    public void calStep(int direction) {
        if(status == STATUS_RUNNING){
            switch (direction)
            {
                case Const.DIRECT_UP:
                    top  -= step ;
                    break ;
                case Const.DIRECT_DOWN:
                    top  += step ;
                    break ;
                case Const.DIRECT_LEFT:
                    left -= step ;
                    break ;
                case Const.DIRECT_RIGHT:
                    left += step ;
                    break ;
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
    }

    public void destroy(){
        if(mBulletTimer != null){
            mBulletTimer.cancel();
        }
    }

    @Override
    public int[] getLeftAndTop() {
        int[] lt = new int[2] ;
        lt[0] = left ;
        lt[1] = top ;
        return lt;
    }
}
