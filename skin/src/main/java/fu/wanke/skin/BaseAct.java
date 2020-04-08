package fu.wanke.skin;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

public class BaseAct extends AppCompatActivity implements
        SkinManager.SkinUpdater
        , SkinManager.DynamicUpdater {


    private SkinFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SkinManager.getInstance().initPage(this);

        LayoutInflater layoutInflater = getLayoutInflater();

        setLayoutInflaterFactory(layoutInflater);

        factory = new SkinFactory();

        LayoutInflaterCompat.setFactory2(layoutInflater,
                factory);

    }


    public void setLayoutInflaterFactory(LayoutInflater original) {
        LayoutInflater layoutInflater = original;
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.set(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SkinManager.getInstance().onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().onDestory(this);
    }

    @Override
    public void apply() {
        factory.apply();
    }

    @Override
    public void applyDynamic(DynamicView dynamicView) {
        factory.applyDynamic(dynamicView);
    }
}
