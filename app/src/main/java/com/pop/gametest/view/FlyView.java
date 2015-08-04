package com.pop.gametest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pop.gametest.sprite.Bullet;
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
    private Bitmap mTile ;
    public static Bitmap sSheet ;
    public static int UNIT ;
    public static int SCALED_UNIT ;

    public static int GAME_REGION_X ;
    public static int GAME_REGION_Y ;

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        L.d(TAG, "FlyView cons.") ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
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

                    long endTime = System.currentTimeMillis();
                    deltaTime = endTime - startTime ;
                    L.d(TAG ,"one draw cal time:"+deltaTime);
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

        for (Tank t: SpriteManager.getInstance().getTanks()){
            t.destroy();
        }
    }

    private boolean isHit(int[] a ,int[] b){
        boolean leftHit = Math.abs(a[0] - b[0]) < SCALED_UNIT ;
        boolean topHit  = Math.abs(a[1] - b[1]) < SCALED_UNIT ;
        if(leftHit && topHit){
            L.d(TAG ,"a:"+ Arrays.toString(a)+":b:"+Arrays.toString(b));
            L.d(TAG ,"isHit:"+leftHit+":"+topHit);
        }
        return leftHit && topHit ;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        L.d(TAG ,"onTouchEvent:"+event.getAction()) ;
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(SpriteManager.getInstance().getTanks().size() < 4){
                    SpriteManager.getInstance().getTanks().add(new Tank()) ;
                }
                break ;
        }
        return true;
    }
}
