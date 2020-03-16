package com.example.pluginproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class Act extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        System.out.println("--------------  ");
        new People();
        new People();
        System.out.println("--------------  ");


        getAllApps();


        launch("cn.samsclub.app");

    }


    private void getAllApps() {


        PackageManager manager = getPackageManager();

        List<ApplicationInfo> installedApplications = manager.getInstalledApplications(0);

        for (ApplicationInfo installedApplication : installedApplications) {
            System.out.println("installedApplication = " + installedApplication.packageName);
        }

    }

    private void launch(String packagename) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packagename);
        // 这里如果intent为空，就说名没有安装要跳转的应用嘛
        if (intent != null) {
            // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
            Toast.makeText(getApplicationContext(), packagename + " , 已安装", Toast.LENGTH_LONG).show();
        } else {
            // 没有安装要跳转的app应用，提醒一下
            Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show();
        }
    }

}

