package com.pop.gametest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by pengfu on 15/7/30.
 */
public class Tank {

    private final static int step = 5;

    private static final int DIRECT_UP    = 0 ;
    private static final int DIRECT_DOWN  = 1 ;
    private static final int DIRECT_LEFT  = 2 ;
    private static final int DIRECT_RIGHT = 3 ;
    public int left ;
    public int top ;

    private Bitmap mTankBmp ;

    private int region_x ;
    private int region_y ;

    private Matrix matrix = new Matrix() ;

    private static float SCALE_SIZE = 2.0F;


    private static int CLIP_UNIT;

    public Tank(int x ,int y) {
        mTankBmp = BitmapFactory.decodeStream(App.getContext().getResources().openRawResource(R.raw.tank)) ;
        CLIP_UNIT = mTankBmp.getWidth()/4  ;
        region_x = (int)(x - CLIP_UNIT*SCALE_SIZE) ;
        region_y = (int)(y - CLIP_UNIT*SCALE_SIZE) ;
        matrix.setScale(SCALE_SIZE, SCALE_SIZE);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(getTankDrawable(getDirection(left, top)), left, top, new Paint());
        calStep();
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
    private int getDirection(float x ,float y){
        int direct = 0 ;
        if( y == 0){
            direct = DIRECT_RIGHT ;
        }else if(x == region_x){
            direct = DIRECT_DOWN ;
        }else if(y == region_y){
            direct = DIRECT_LEFT ;
        }else if(x == 0){
            direct = DIRECT_UP ;
        }
        return direct ;
    }

    private void calStep() {
        if (left < region_x && top == 0) {
            left += step;
        } else if (left == region_x && top < region_y) {
            top += step;
        } else if (left <= region_x && left > 0 && top == region_y) {
            left -= step;
        } else if (left == 0 && top <= region_y && top > 0) {
            top -= step;
        }
        if (left > region_x) {
            left = region_x;
        }
        if (top > region_y) {
            top = region_y;
        }
        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
    }

}
