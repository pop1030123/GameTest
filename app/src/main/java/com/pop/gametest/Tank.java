package com.pop.gametest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.pop.gametest.view.FlyView;

/**
 * Created by pengfu on 15/7/30.
 */
public class Tank {

    private final static int step = 5;

    private static final int DIRECT_UP    = 0 ;
    private static final int DIRECT_DOWN  = 1 ;
    private static final int DIRECT_LEFT  = 2 ;
    private static final int DIRECT_RIGHT = 3 ;
    private static final String TAG = "Tank:";
    public int left ;
    public int top ;

    private int region_x ;
    private int region_y ;
    private int anim_index ;

    private Matrix matrix = new Matrix() ;

    private static float SCALE_SIZE = 2.0F;
    private static int ANIM_SIZE = 8;

    private static int CLIP_UNIT;

    public Tank(int x ,int y) {

        CLIP_UNIT = FlyView.UNIT;
        region_x = (int)(x - CLIP_UNIT*SCALE_SIZE) ;
        region_y = (int)(y - CLIP_UNIT*SCALE_SIZE) ;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(getTankDrawable(getDirection(left, top)), left, top, new Paint());
        calStep();
    }

    private Bitmap getTankDrawable(int direction){
        Bitmap bmp = null ;
        int[] left_top = getAnimPos() ;
        matrix.reset();
        matrix.setScale(SCALE_SIZE, SCALE_SIZE);
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
