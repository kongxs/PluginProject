package com.example.pluginproject.data;

import android.content.Context;
import android.widget.Toast;

import com.example.pluginproject.Modify;
import com.example.pluginproject.TestLog;

public class People {

    private String name = "k";

    @Modify
    public int getAge(int age) {
        new TestLog("cons invoked ").log("hello world :  " + age , "hello world 2");
        return 10;
    }


    public void println(Context context , String message) {
        context.getResources().getIdentifier("img_bg","drawable",context.getPackageName());
        Toast.makeText(context , message , Toast.LENGTH_SHORT).show();
    }

}
