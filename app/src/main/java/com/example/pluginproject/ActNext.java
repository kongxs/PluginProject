package com.example.pluginproject;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

public class ActNext extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_act);

        System.out.println("--------------  ");
        new People();
        new People();
        System.out.println("--------------  ");
    }

}

