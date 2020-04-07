package com.example.pluginproject.attrs;

import android.view.View;

public abstract class SkinAttr {

    /**
     *  <textview
     *      androidi: tetxtcolor = "R.color.xxx"
     *
     *
     * attrName : tetxtcolor
     * id: @1111 ->  1111
     * entryName :  xxx
     * typeName: color
     *
     *
     */


    protected String attrName;
    protected int id;
    protected String entryName;
    protected String typeName;
//    private Resources mResources;
//    private Context context;
//    private String mSkinPackageName;

    public void init(String attrName, int id, String entryName, String typeName) {
        this.attrName = attrName;
        this.id = id;
        this.entryName = entryName;
        this.typeName = typeName;


    }


    public abstract void apply(View view);


    public String getAttrName() {
        return attrName;
    }

    public int getId() {
        return id;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getTypeName() {
        return typeName;
    }


}
