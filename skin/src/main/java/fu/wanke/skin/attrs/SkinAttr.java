package fu.wanke.skin.attrs;

import android.content.Context;
import android.view.View;

import fu.wanke.skin.SkinManager;

public abstract class SkinAttr {


    public static final String TEXT_COLOR = "textColor";

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

    public static SkinAttr newInstance(String attrName  , int refResId) {

        int id = refResId;

        Context context = SkinManager.getInstance().getmContext();

        String entryName = context.getResources().getResourceEntryName(id);
        String typeName = context.getResources().getResourceTypeName(id);

        return SkinAttrFactory.get(attrName, id, entryName, typeName);
    }



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
