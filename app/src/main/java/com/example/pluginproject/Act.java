package com.example.pluginproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

import fu.wanke.skin.BaseAct;
import fu.wanke.skin.SkinManager;

public class Act extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_act);

        System.out.println("--------------  ");
        new People();
        new People();
        System.out.println("--------------  ");


//        getAllApps()
//        launch("cn.samsclub.app");



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().load(Environment.getExternalStorageDirectory()
                        + File.separator + "red.skin", new SkinManager.OnSkinLoadInterface() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish(boolean success) {

                    }
                });
            }
        });

        findViewById(R.id.deef).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().restoreDefault();
            }
        });


        findViewById(R.id.tonext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Act.this,ActNext.class));
            }
        });
    }

}

