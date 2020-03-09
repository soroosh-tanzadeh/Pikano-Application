package com.ternobo.pikano.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ternobo.pikano.database.ResponseProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseAcitivty extends AppCompatActivity {

    public static BaseAcitivty current;
    protected Map<String, ResponseProgress> downloads = new HashMap<>();
    private ArrayList<BaseAcitivty> acitivties = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        forceRTLIfSupported();
        acitivties.add(this);
        BaseAcitivty.current = this;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void forceRTLIfSupported(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void finishAll() {
        for (BaseAcitivty acitivty : acitivties) {
            acitivty.finish();
        }
        acitivties = new ArrayList<>();
    }

}