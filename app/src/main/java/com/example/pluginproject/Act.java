package com.example.pluginproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.pluginproject.mvp.MvpExample;
import com.example.pluginproject.retrofit.NetInterface;
import com.example.pluginproject.retrofit.ReqResult;

import java.io.File;

import fu.wanke.skin.BaseAct;
import fu.wanke.skin.SkinManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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



        Retrofit retrofit = new Retrofit.Builder()
                .validateEagerly(true)
                .baseUrl("https://www.mxnzp.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        NetInterface service = retrofit.create(NetInterface.class);

        Call<ReqResult> call = service.getCall();


        call.enqueue(new Callback<ReqResult>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ReqResult> call, Response<ReqResult> response) {
                //请求处理,输出结果
                Toast.makeText(Act.this, response.body().getMsg() , Toast.LENGTH_SHORT).show();
            }
            //请求失败时候的回调
            @Override
            public void onFailure(Call<ReqResult> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

        //同步请求
//        try {
//            Response<ReqResult> response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}

