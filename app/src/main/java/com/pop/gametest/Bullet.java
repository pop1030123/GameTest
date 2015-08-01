package com.pop.gametest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.pop.gametest.view.FlyView;

/**
 * Created by pengfu on 15/7/30.
 */
public class Bullet {

    private int left ;
    private int top ;

    private static int STEP = FlyView.UNIT ;
    private Callback mCallback ;

    private static Bitmap sBmp ;
    static{
        Matrix matrix = new Matrix() ;
        matrix.postScale(FlyView.SCALE_SIZE ,FlyView.SCALE_SIZE) ;
        sBmp = Bitmap.createBitmap(FlyView.sSheet ,FlyView.UNIT*5,FlyView.UNIT*2,FlyView.UNIT,FlyView.UNIT ,matrix ,false);
    }

    public Bullet(int l ,int t ,Callback c){
        left = l ;
        top = t ;
        mCallback = c ;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(sBmp ,left ,top ,new Paint());
        calStep();
    }

    private void calStep(){
        if(top == 0){
            left += STEP ;
        }else if (left == Tank.REGION_X){
            top  += STEP ;
        }else if (top == Tank.REGION_Y){
            left -= STEP ;
        }else if(left == 0){
            top -= STEP ;
        }

        if(left > Tank.REGION_X || top > Tank.REGION_Y || left<0 || top<0){
            mCallback.onOutRegion(left ,top);
        }
    }

    public interface Callback{
        void onOutRegion(int left, int top) ;
    }
}
