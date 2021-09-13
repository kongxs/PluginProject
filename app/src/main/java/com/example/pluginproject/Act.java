package com.example.pluginproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.pluginproject.data.People;
import com.example.pluginproject.mvp.MvpExample;

import java.io.File;
import java.lang.reflect.Field;

import fu.wanke.link.LinkManager;
import fu.wanke.skin.BaseAct;
import fu.wanke.skin.SkinManager;

public class Act extends BaseAct {

    private People people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_act);

        System.out.println("--------------  ");
        people = new People();
        Toast.makeText(this,""+people.getAge(10),Toast.LENGTH_SHORT).show();
//        new People();
        System.out.println("--------------  ");

        people.println(this,"hello println");

//        getAllApps()
//        launch("cn.samsclub.app");


        final String skinPath = Environment.getExternalStorageDirectory()
                + File.separator + "red.skin";


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SkinManager.getInstance().load(skinPath, new SkinManager.OnSkinLoadInterface() {
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

        findViewById(R.id.mvp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Act.this, MvpExample.class));
            }
        });

        findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkManager.getInstance().register();
            }
        });

        findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkManager.getInstance().connection();
            }
        });

        findViewById(R.id.connectoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinkManager.getInstance().sendMessage(MessagePage.mockConnection());

                PeoRedirect redirect = new PeoRedirect();
                try {
//                    Field quickRedirect = people.getClass().getDeclaredField("changeQuickRedirect");
//
//                    quickRedirect.set(null ,redirect);


//                    int age = people.getAge(10);
                    Toast.makeText(Act.this,""+people.getAge(10),Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

}

