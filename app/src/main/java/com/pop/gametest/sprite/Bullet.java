package com.pop.gametest.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.pop.gametest.Const;
import com.pop.gametest.view.FlyView;

/**
 * Created by pengfu on 15/7/30.
 */
public class Bullet implements Sprite {

    private int left ;
    private int top ;
    private int direction ;

    private static int STEP = FlyView.UNIT/2 ;
    private Callback mCallback ;

    public static int BULLET_SIZE ;
    private static int BULLET_EXTRA_SIZE ;
    private static Bitmap sBmp ;
    static{
        Matrix matrix = new Matrix() ;
        matrix.postScale(FlyView.SCALE_SIZE ,FlyView.SCALE_SIZE) ;
        int clip_unit = FlyView.UNIT/3 ;
        BULLET_SIZE = (int)(clip_unit * FlyView.SCALE_SIZE) ;
        BULLET_EXTRA_SIZE = FlyView.SCALED_UNIT/2 - BULLET_SIZE/2 ;
        sBmp = Bitmap.createBitmap(FlyView.sSheet ,FlyView.UNIT*5+clip_unit,FlyView.UNIT*2+clip_unit,FlyView.UNIT-clip_unit,FlyView.UNIT-clip_unit ,matrix ,false);
    }

    public Bullet(int l ,int t ,int direction ,Callback c){
        // shoot at head of tank
        switch (direction)
        {
            case Const.DIRECT_UP:
                t -= FlyView.SCALED_UNIT ;
                break ;
            case Const.DIRECT_DOWN:
                t += FlyView.SCALED_UNIT ;
                break ;
            case Const.DIRECT_LEFT:
                l -= FlyView.SCALED_UNIT ;
                break ;
            case Const.DIRECT_RIGHT:
                l += FlyView.SCALED_UNIT ;
                break ;
        }
        left = l ;
        top = t ;
        this.direction = direction ;
        mCallback = c ;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(sBmp, left+BULLET_EXTRA_SIZE, top+BULLET_EXTRA_SIZE, new Paint());
    }

    public void calStep(){
        switch (direction){
            case Const.DIRECT_UP:
                top -= STEP ;
                break ;
            case Const.DIRECT_DOWN:
                top += STEP ;
                break ;
            case Const.DIRECT_LEFT:
                left -= STEP ;
                break ;
            case Const.DIRECT_RIGHT:
                left += STEP ;
                break ;
        }
        if(left > Tank.REGION_X || top > Tank.REGION_Y || left<0 || top<0){
            mCallback.onOutRegion(this);
        }
    }

    @Override
    public int[] getLeftAndTop() {
        int[] lt = new int[2] ;
        lt[0] = left ;
        lt[1] = top ;
        return lt;
    }

    public interface Callback{
        void onOutRegion(Bullet b) ;
    }
}
