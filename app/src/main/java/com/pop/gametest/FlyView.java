package com.pop.gametest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;

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

    private final static int radius = 25;
    private final static int step = 10;
    private final static int SPRITE_COL = 4;
    private final static int SPRITE_ROW = 4;

    private Canvas canvas;

    private int x = radius, y = radius;
    private Paint paint;
    private int spriteWidth;
    private int spriteHeight;
    private Context mContext ;
    private FrameAnimation []spriteAnimations;
    private Sprite mSprite;
    private float spriteSpeed = (float)((500  * App.SCREEN_WIDTH / 480) * 0.001);


    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context ;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        initResources() ;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        flag = true;
        width = getMeasuredWidth() - radius;
        height = getMeasuredHeight() - radius;
        mSprite = new Sprite(spriteAnimations,0,0,spriteWidth,spriteHeight,spriteSpeed ,width ,height);

        flyThread = new Thread(this);
        flyThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
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

                    mSprite.setDirection();
                    mSprite.updatePosition(deltaTime);
                    canvas.drawColor(Color.BLACK);
                    mSprite.draw(canvas);

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
            }
        }
    }

    private void calStep() {
        if (x < width && y == radius) {
            x += step;
        } else if (x == width && y < height) {
            y += step;
        } else if (x <= width && x > radius && y == height) {
            x -= step;
        } else if (x == radius && y <= height && y > radius) {
            y -= step;
        }
        if (x > width) {
            x = width;
        }
        if (y > height) {
            y = height;
        }
        if (x < radius) {
            x = radius;
        }
        if (y < radius) {
            y = radius;
        }
    }

    private void initResources() {
        Bitmap[][] spriteImgs = generateBitmapArray(mContext, R.drawable.tank, SPRITE_ROW, SPRITE_COL);
        spriteAnimations = new FrameAnimation[SPRITE_ROW];
        for(int i = 0; i < SPRITE_ROW; i ++) {
            Bitmap []spriteImg = spriteImgs[i];
            FrameAnimation spriteAnimation = new FrameAnimation(spriteImg,new int[]{150,150,150,150},true);
            spriteAnimations[i] = spriteAnimation;
        }
    }

    public Bitmap decodeBitmapFromRes(Context context, int resourseId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resourseId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public Bitmap createBitmap(Context context, Bitmap source, int row,
                               int col, int rowTotal, int colTotal) {
        Bitmap bitmap = Bitmap.createBitmap(source,
                (col - 1) * source.getWidth() / colTotal,
                (row - 1) * source.getHeight() / rowTotal, source.getWidth()
                        / colTotal, source.getHeight() / rowTotal);
        return bitmap;
    }

    public Bitmap[][] generateBitmapArray(Context context, int resourseId,
                                          int row, int col) {
        Bitmap bitmaps[][] = new Bitmap[row][col];
        Bitmap source = decodeBitmapFromRes(context, resourseId);
        this.spriteWidth = source.getWidth() / col;
        this.spriteHeight = source.getHeight() / row;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                bitmaps[i - 1][j - 1] = createBitmap(context, source, i, j,
                        row, col);
            }
        }
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        return bitmaps;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
