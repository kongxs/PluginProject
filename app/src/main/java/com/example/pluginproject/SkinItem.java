package com.example.pluginproject;

import android.view.View;

import com.example.pluginproject.attrs.SkinAttr;

import java.util.List;

public class SkinItem {
    public View view;
    public List<SkinAttr> attrs;

    public void apply() {
        if (attrs != null && attrs.size() > 0) {
            for (SkinAttr attr : attrs) {
                attr.apply(view);
            }
        }
    }
}
