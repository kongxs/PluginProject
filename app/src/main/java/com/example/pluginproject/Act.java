package com.example.pluginproject;

import android.app.Activity;
import android.os.Bundle;

public class Act extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        System.out.println("--------------  ");
        new People();
        new People();
        System.out.println("--------------  ");


    }
}

