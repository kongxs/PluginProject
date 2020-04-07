package fu.wanke.skin;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import fu.wanke.skin.attrs.SkinAttr;
import fu.wanke.skin.attrs.SkinAttrFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {



    /**
     * 属性处理类
     */

    /**
     * 保存view的构造方法
     */
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    public  static final String NAMESPACE = "http://schemas.android.com/android/skin";
    private static final String ATTR_SKIN_ENABLE = "enable";


    public final String[] a = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private List<SkinItem> mSkinItems = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {

        System.out.println("onCreateView : " + name);

        boolean isSkinEnable = attributeSet.getAttributeBooleanValue(NAMESPACE, ATTR_SKIN_ENABLE, false);

        if (!isSkinEnable) {
            return null;
        }

        View view = createViewFormTag(name, context, attributeSet);
        if (view == null) {
            view = createView(name, context, attributeSet);
        }

        if (view == null){
            return null;
        }

        parseSkinAttr(context, attributeSet, view);


        return view;
    }

    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {

        List<SkinAttr> viewAttrs  = new ArrayList<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++){

            String attrName = attrs.getAttributeName(i);   // name： background，textColor 等
            String attrValue = attrs.getAttributeValue(i); // 属性的值  @1122222 这种格式

            if(!SkinAttrFactory.isSupportedAttr(attrName)){ //检查 SDK 是否支持这些属性
                continue;
            }

            if(attrValue.startsWith("@")){ // 当设置了通过R.xxx.xxx引用的方式，我们支持更新资源
                try {
                    int id = Integer.parseInt(attrValue.substring(1)); // 获取到 值
                    //获取到属性的 string name @color/  [colorAccent]
                    String entryName = context.getResources().getResourceEntryName(id);
                    //属性的类型，比如 color
                    String typeName = context.getResources().getResourceTypeName(id);

                    SkinAttr mSkinAttr = SkinAttrFactory.get(attrName, id, entryName, typeName);

                    if (mSkinAttr != null) {
                        viewAttrs.add(mSkinAttr);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }



        if(viewAttrs.size() > 0){
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;

            mSkinItems.add(skinItem);

            if(SkinManager.getInstance().isDoUpdate()){
                skinItem.apply();
            }
        }


    }

    /**
     * 参考LayoutInflater源码
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private View createViewFormTag(String name, Context context, AttributeSet attrs) {
        //包含自定义控件
        if (-1 != name.indexOf('.')) {
            return null;
        }
        View view = null;
        for (int i = 0; i < a.length; i++) {
            view = createView(a[i] + name, context, attrs);
            if (view != null) {
                break;
            }
        }
        return view;
    }


    /**
     * 参考LayoutInflater源码
     * 获取构造函数，创建view
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 参考LayoutInflater源码
     * 通过反射获取View构造函数
     *
     * @param context
     * @param name
     * @return
     */
    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (null == constructor) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass
                        (name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }



    public void apply() {
        if(mSkinItems != null && mSkinItems.size() > 0) {
            for (SkinItem item : mSkinItems) {
                item.apply();
            }
        }
    }
}
