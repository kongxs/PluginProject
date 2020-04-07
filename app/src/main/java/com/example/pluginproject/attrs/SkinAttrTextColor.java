package com.example.pluginproject.attrs;

import android.view.View;
import android.widget.TextView;

import com.example.pluginproject.SkinManager;

public class SkinAttrTextColor extends SkinAttr{

    @Override
    public void apply(View view) {

        if (SkinAttrFactory.TEXT_COLOR.equals(this.attrName)) {

            int color = SkinManager.getInstance().getColor(this);

            if (color != -1 && view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
        }
    }
}
