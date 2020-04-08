package com.example.pluginproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fu.wanke.skin.BaseAct;
import fu.wanke.skin.DynamicView;
import fu.wanke.skin.attrs.SkinAttr;

public class ActNext extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.layout_act);
//
//        System.out.println("--------------  ");
//        new People();
//        new People();
//        System.out.println("--------------  ");


        View view = new TextView(this);
        ((TextView) view).setText("hello world");

        setContentView(view);


        applyDynamic(
                new DynamicView.Builder(view)
                        .addAttribute(
                    SkinAttr.newInstance(SkinAttr.TEXT_COLOR , R.color.color_main_text)
        ).build());

    }

}

