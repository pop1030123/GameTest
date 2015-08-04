package com.pop.gametest.sprite;

import com.pop.gametest.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengfu on 15/8/3.
 */
public class BulletManager implements Bullet.Callback {


    private static final String TAG = "BulletManager:";
    private static BulletManager manager;

    private List<Bullet> mBullets = new ArrayList<Bullet>();

    private BulletManager(){}

    public static BulletManager getInstance(){
        if(manager == null){
            manager = new BulletManager() ;
        }
        return manager ;
    }

    public List<Bullet> getBullets(){
        return mBullets ;
    }

    @Override
    public void onOutRegion(Bullet b) {
        L.d(TAG, "onOutRegion:" + b) ;
        mBullets.remove(b) ;
    }
}
