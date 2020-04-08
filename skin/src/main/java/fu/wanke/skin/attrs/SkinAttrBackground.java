package fu.wanke.skin.attrs;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import fu.wanke.skin.SkinManager;

public class SkinAttrBackground extends SkinAttr{

    @Override
    public void apply(View view) {

        if (ATTR_BACKGROUND.equals(this.attrName)) {

            if ("color".equals(typeName)) {
                int color = SkinManager.getInstance().getColor(this);

                if (color != -1) {
                    (view).setBackgroundColor(color);
                }
            } else if ("drawable".equals(typeName)) {
                Drawable drawable = SkinManager.getInstance().getDrawable(this , "drawable");
                if (drawable != null) {
                    view.setBackground(drawable);
                }
            } else if ("mipmap".equals(typeName)) {
                Drawable drawable = SkinManager.getInstance().getDrawable(this , "mipmap");
                if (drawable != null) {
                    view.setBackground(drawable);
                }
            }
        }
    }
}
