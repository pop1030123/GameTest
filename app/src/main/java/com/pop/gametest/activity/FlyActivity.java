package com.pop.gametest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pop.gametest.R;
import com.pop.gametest.view.FlyView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by pengfu on 15/7/27.
 */
@EActivity(R.layout.act_fly)
public class FlyActivity extends Activity implements View.OnClickListener {

    @ViewById(R.id.toFly2)
    Button mToFly2Btn;
    @ViewById(R.id.flyView)
    FlyView mFlyView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews(){
        mToFly2Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toFly2:
                Intent toFly2Intent = new Intent() ;
                toFly2Intent.setClass(this, Fly2Activity_.class) ;
                startActivity(toFly2Intent);
                break ;
        }
    }
}
