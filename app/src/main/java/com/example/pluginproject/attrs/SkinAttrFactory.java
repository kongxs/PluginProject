package com.example.pluginproject.attrs;

import java.util.HashMap;
import java.util.Map;

public class SkinAttrFactory {


    public static final String TEXT_COLOR = "textColor";


    private static Map<String , Class<? extends SkinAttr>> maps = new HashMap<>();

    static {
        maps.put(TEXT_COLOR , SkinAttrTextColor.class);
    }



    public static SkinAttr get(String attrName, int id, String entryName, String typeName) {

        SkinAttr mSkinAttr = null;

        try {
            Class<? extends SkinAttr> aClass = maps.get(attrName);
            mSkinAttr = aClass.newInstance();
        } catch ( Exception e) {
        }

        if (mSkinAttr != null) {
            mSkinAttr.init(attrName, id, entryName, typeName);
        }

        return mSkinAttr;
    }


    public static boolean isSupportedAttr(String attrName){
        return maps.get(attrName)!= null;
    }


}
