package com.pop.gametest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pop.gametest.Const;
import com.pop.gametest.sprite.Bullet;
import com.pop.gametest.sprite.MyTank;
import com.pop.gametest.sprite.SpriteManager;
import com.pop.gametest.L;
import com.pop.gametest.R;
import com.pop.gametest.sprite.Explode;
import com.pop.gametest.sprite.Tank;

import java.util.Arrays;

/**
 * Created by pengfu on 15/7/20.
 */
public class FlyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FlyView:";
    private SurfaceHolder holder;
    private Thread flyThread;
    private boolean flag;

    private Canvas canvas;
    private Paint paint;

    public static float SCALE_SIZE = 2.0F;
    public static int TOUCH_DISTANCE = 70;
    private Bitmap mTile ;
    private static Bitmap mControlView ;
    private static float control_size ;
    public static Bitmap sSheet ;
    public static int UNIT ;
    public static int SCALED_UNIT ;

    public static int GAME_REGION_X ;
    public static int GAME_REGION_Y ;

    private MyTank myTank ;

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        L.d(TAG, "FlyView cons.") ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        Matrix matrix = new Matrix() ;
        matrix.postScale(SCALE_SIZE ,SCALE_SIZE) ;
        Bitmap control = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.control)) ;
        mControlView = Bitmap.createBitmap(control ,0,0,control.getWidth() ,control.getHeight() ,matrix ,false) ;
        control_size = mControlView.getWidth() ;
        sSheet = BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.tanks_sheet)) ;
        UNIT = sSheet.getWidth() / 8 ;
        SCALED_UNIT = (int)(UNIT * SCALE_SIZE) ;
        mTile = Bitmap.createBitmap(sSheet,0,0,UNIT ,UNIT) ;
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        L.d(TAG ,"surfaceCreated:"+surfaceHolder) ;
        flag = true;
        GAME_REGION_X = getWidth();
        GAME_REGION_Y = getHeight();
        SpriteManager.getInstance().getTanks().add(new Tank()) ;
        myTank = new MyTank() ;
        flyThread = new Thread(this);
        flyThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        L.d(TAG, "surfaceChanged:" + surfaceHolder + ":format:" + format + ":width:" + width + ":height:" + height) ;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        L.d(TAG ,"surfaceDestroyed:"+surfaceHolder) ;
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
                    while (left < GAME_REGION_X){
                        while (top < GAME_REGION_Y){
                            canvas.drawBitmap(mTile ,left ,top ,paint);
                            top += UNIT ;
                        }
                        top = 0 ;
                        left += UNIT ;
                    }
                    // drawing
                    for (Tank t : SpriteManager.getInstance().getTanks()){
                        t.draw(canvas);
                    }
                    for (Bullet b: SpriteManager.getInstance().getBullets()){
                        b.draw(canvas);
                    }
                    for (Explode e: SpriteManager.getInstance().getExplodes()){
                        e.draw(canvas);
                    }
                    myTank.draw(canvas ,mDirection);
                    if(isTouch){
                        canvas.drawBitmap(mControlView ,pos_x-control_size/2,pos_y-control_size/2,paint);
                    }
                    // calculate
                    for (Bullet b: SpriteManager.getInstance().getBullets()){
                        if(SpriteManager.getInstance().isExpired(b)){
                            continue;
                        }
                        for (Tank t: SpriteManager.getInstance().getTanks()){
                            if(SpriteManager.getInstance().isExpired(t)){
                                continue;
                            }
                            if(isHit(t.getLeftAndTop() ,b.getLeftAndTop())){
                                SpriteManager.getInstance().getExplodes().add(new Explode(t.left ,t.top , SpriteManager.getInstance())) ;
                                SpriteManager.getInstance().addExpired(t);
                                SpriteManager.getInstance().addExpired(b);
                            }
                        }
                    }

                    SpriteManager.getInstance().clearExpired();

                    for (Tank t : SpriteManager.getInstance().getTanks()){
                        t.calStep();
                    }
                    // TODO :ConcurrentModificationException here because Removing and foreach doing at the same time.
                    for (Bullet b: SpriteManager.getInstance().getBullets()){
                        b.calStep();
                    }
                    myTank.calStep(mDirection);
                    if(SpriteManager.getInstance().getTanks().size() < 1){
                        SpriteManager.getInstance().getTanks().add(new Tank()) ;
                    }

                    long endTime = System.currentTimeMillis();
                    deltaTime = endTime - startTime ;
//                    L.d(TAG ,"one draw cal time:"+deltaTime);
                    if (deltaTime < 50) {
                        Thread.currentThread().sleep(50 - deltaTime);
                    }
                }
            } catch (Exception e) {
                L.d(TAG, "e:" + e);
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
        SpriteManager.getInstance().reset();
        myTank.destroy();
    }

    private boolean isHit(int[] a ,int[] b){
        boolean leftHit = Math.abs(a[0] - b[0]) < Bullet.BULLET_SIZE ;
        boolean topHit  = Math.abs(a[1] - b[1]) < Bullet.BULLET_SIZE ;
        if(leftHit && topHit){
            L.d(TAG ,"a:"+ Arrays.toString(a)+":b:"+Arrays.toString(b));
            L.d(TAG ,"isHit:"+leftHit+":"+topHit);
        }
        return leftHit && topHit ;
    }
    private boolean isTouch  ;
    private float pos_x ;
    private float pos_y ;
    private float cur_x ;
    private float cur_y ;

    private int mDirection = Const.DIRECT_UP ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch = true ;
                pos_x = event.getX() ;
                pos_y = event.getY() ;
                myTank.setStatus(MyTank.STATUS_RUNNING);
                break ;
            case MotionEvent.ACTION_MOVE:
                isTouch = true ;
                cur_x = event.getX() ;
                cur_y = event.getY() ;
                mDirection = getDirection(cur_x, cur_y) ;
                break ;
            case MotionEvent.ACTION_UP:
                isTouch = false ;
                myTank.setStatus(MyTank.STATUS_UNKNOWN);
                break ;
        }
        return true;
    }


    private int getDirection(float x ,float y){
        int direction = Const.DIRECT_UP ;
        float dir_x = x - pos_x ;
        float dir_y = y - pos_y ;
        if(dir_x > TOUCH_DISTANCE){
            direction = Const.DIRECT_RIGHT ;
        }else if(dir_x < -TOUCH_DISTANCE){
            direction = Const.DIRECT_LEFT ;
        }else if(dir_y > TOUCH_DISTANCE){
            direction = Const.DIRECT_DOWN ;
        }else if(dir_y < -TOUCH_DISTANCE){
            direction = Const.DIRECT_UP ;
        }
        return direction ;
    }
}
