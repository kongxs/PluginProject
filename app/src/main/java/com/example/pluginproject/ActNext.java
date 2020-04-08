package com.example.pluginproject;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fu.wanke.skin.BaseAct;
import fu.wanke.skin.DynamicView;
import fu.wanke.skin.SkinManager;
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

//        setContentView(view);


        applyDynamic(
                new DynamicView.Builder(view)
                        .addAttribute(
                    SkinAttr.newInstance(SkinAttr.ATTR_TEXT_COLOR , R.color.color_main_text)
        ).build());




        XmlResourceParser layout = SkinManager.getInstance().getLayout("layout");

        if (layout != null) {
            View inflate = LayoutInflater.from(this).inflate(layout, null);

            if (inflate != null) {
                setContentView(inflate);
            }
        }



    }

}

