package com.pop.gametest.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.pop.gametest.L;
import com.pop.gametest.view.FlyView;

/**
 * Created by pengfu on 15/8/4.
 */
public class Explode {

    private static final String TAG = "Explode:";
    private int left ;
    private int top  ;

    private static final int ANIM_SIZE = 3 ;
    private int anim_index ;

    private Callback mCallback ;
    private static Matrix matrix = new Matrix();
    static {
        matrix.setScale(FlyView.SCALE_SIZE, FlyView.SCALE_SIZE);
    }

    public Explode(int l ,int t ,Callback c){
        L.d(TAG ,"Explode:"+l+"X"+t);
        left =  l ;
        top  = t ;
        mCallback = c ;
    }

    public void draw(Canvas canvas){
        int[] left_top = getAnimPos() ;
        canvas.drawBitmap(getBitmapDrawable(left_top) ,left ,top ,new Paint());
        if (anim_index == ANIM_SIZE){
            if(mCallback != null){
                mCallback.onExplodeEnd(this);
            }
        }
    }

    private Bitmap getBitmapDrawable(int[] left_top){
        return Bitmap.createBitmap(FlyView.sSheet ,left_top[0] ,left_top[1] ,FlyView.UNIT ,FlyView.UNIT,matrix ,false) ;
    }

    private int[] getAnimPos(){
        int[] left_top = new int[2] ;
        anim_index = anim_index % ANIM_SIZE ;
        left_top[0] = (anim_index+1)*FlyView.UNIT ;
        left_top[1] = 2*FlyView.UNIT ;
        anim_index ++ ;
        return  left_top ;
    }

    public interface Callback{
        void onExplodeEnd(Explode explode);
    }
}
