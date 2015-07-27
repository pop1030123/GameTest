package com.pop.gametest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pop.gametest.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.act_main)
public class MainActivity extends Activity implements View.OnClickListener {

    @ViewById(R.id.toFly)
    Button mToFly ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterViews(){
        mToFly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toFly:
                Intent toFlyIntent = new Intent() ;
                toFlyIntent.setClass(this ,FlyActivity_.class) ;
                startActivity(toFlyIntent);
                break ;
        }
    }
}
