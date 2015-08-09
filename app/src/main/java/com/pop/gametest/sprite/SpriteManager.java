package com.pop.gametest.sprite;

import com.pop.gametest.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengfu on 15/8/3.
 */
public class SpriteManager implements Bullet.Callback ,Explode.Callback {


    private static final String TAG = "SpriteManager:";
    private static SpriteManager manager;

    private List<Bullet> mBullets = new ArrayList<Bullet>();
    private List<Bullet> mExpiredBullets = new ArrayList<Bullet>();

    private List<Tank> mTanks = new ArrayList<Tank>() ;
    private List<Tank> mExpiredTanks = new ArrayList<Tank>() ;

    private List<Explode> mExplodes = new ArrayList<Explode>();
    private SpriteManager(){}

    public static SpriteManager getInstance(){
        if(manager == null){
            manager = new SpriteManager() ;
        }
        return manager ;
    }

    public List<Bullet> getBullets(){
        return mBullets ;
    }
    public List<Explode> getExplodes(){
        return mExplodes ;
    }
    public List<Tank> getTanks(){
        return mTanks ;
    }

    public void addExpired(Bullet bullet){
        mExpiredBullets.add(bullet) ;
    }
    public void addExpired(Tank tank){
        mExpiredTanks.add(tank) ;
    }
    public boolean isExpired(Bullet bullet){
        return mExpiredBullets.contains(bullet) ;
    }
    public boolean isExpired(Tank tank){
        return mExpiredTanks.contains(tank) ;
    }

    @Override
    public void onOutRegion(Bullet b) {
//        L.d(TAG, "onOutRegion:" + b) ;
        addExpired(b);
    }

    @Override
    public void onExplodeEnd(Explode explode) {
        L.d(TAG ,"onExplodeEnd:"+explode);
        getExplodes().remove(explode) ;
    }

    public void clearExpired(){
        if(mExpiredBullets.size()>0){
            L.d(TAG ,"clearExpired bullet:"+mExpiredBullets.size());
            getBullets().removeAll(mExpiredBullets) ;
            mExpiredBullets.clear();
        }
        if(mExpiredTanks.size()>0){
            L.d(TAG ,"clearExpired tank:"+mExpiredTanks.size());
            for (Tank t:mExpiredTanks){
                t.destroy();
            }
            getTanks().removeAll(mExpiredTanks) ;
            mExpiredTanks.clear();
        }
    }
    public void reset(){
        clearExpired();
        for (Tank t:mTanks){
            t.destroy();
        }
        mTanks.clear();
        mBullets.clear();
        mExplodes.clear();
    }
}
