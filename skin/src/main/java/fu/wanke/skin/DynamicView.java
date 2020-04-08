package fu.wanke.skin;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fu.wanke.skin.attrs.SkinAttr;

public class DynamicView {

    private View view ;

    private List<SkinAttr> attrs = null;

    private DynamicView(Builder builder) {
        this.view = builder.getView();
        if (view == null) throw new NullPointerException("view must not null ");
        attrs = builder.getAttrs();
    }


    public View getView() {
        return view;
    }

    public List<SkinAttr> getAttrs() {
        return attrs;
    }

    private DynamicView setAttrs(List<SkinAttr> attrs) {
        this.attrs = attrs;
        return this;
    }

    public static final class Builder {

        private View view;
        private List<SkinAttr> attrs ;

        public Builder(View view) {
            this.view = view;
            attrs = new ArrayList<>();
        }

        public Builder addAttribute(SkinAttr attr) {
            attrs.add(attr);
            return this;
        }

        public DynamicView build() {
            DynamicView dynamicView = new DynamicView(this)
                    .setAttrs(attrs);
            return dynamicView;
        }


        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public List<SkinAttr> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<SkinAttr> attrs) {
            this.attrs = attrs;
        }
    }

}
