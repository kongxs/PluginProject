package fu.wanke.skin;

import android.view.View;

import fu.wanke.skin.attrs.SkinAttr;

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
