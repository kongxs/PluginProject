package com.example.pluginproject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.example.pluginproject.attrs.SkinAttr;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SkinManager {

    private static final SkinManager instace = new SkinManager();

    private List<SkinUpdater> mSkinUpdaters = new ArrayList<>();


    private Context mContext;

    private boolean isDefaultSkin;

    private String mSkinPath; // 皮肤包的地址

    private Resources mResources; //皮肤包的 资源

    private String mSkinPackageName;

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        return instace;
    }

    public void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context must not nullable ");
        }

        this.mContext = context.getApplicationContext();

        load(gePath() , null);
    }


    public void load(final String skinPath ,final OnSkinLoadInterface callback) {

        new AsyncTask<String, Void, Resources>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            protected Resources doInBackground(String... strings) {

                Resources skinResource = null;
                try {
                    File file = new File(skinPath);
                    if(file == null || !file.exists()){
                        return null;
                    }

                    PackageManager mPm = mContext.getPackageManager();
                    PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                    mSkinPackageName = mInfo.packageName;

                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                    addAssetPath.invoke(assetManager, skinPath);

                    Resources superRes = mContext.getResources();
                    skinResource = new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());


                    mSkinPath = skinPath;

                    savePath(mSkinPath);

                    isDefaultSkin = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return skinResource;
            }

            @Override
            protected void onPostExecute(Resources resources) {
                super.onPostExecute(resources);

                mResources = resources;

                if (callback != null)
                    callback.onFinish(resources != null);

                if (mResources != null) {
                    notifySkinUpdate();
                }
            }
        }.execute(skinPath) ;
    }

    private void notifySkinUpdate() {

        for (int i = 0; i < mSkinUpdaters.size(); i++) {
            mSkinUpdaters.get(i).apply();
        }
    }

    private void savePath(String path) {
        mContext.getSharedPreferences("skin_path",Context.MODE_PRIVATE)
                .edit().putString("skin_path_val" , path).commit();
    }

    private String gePath() {
        return mContext.getSharedPreferences("skin_path",Context.MODE_PRIVATE)
                .getString("skin_path_val","");
    }


    public boolean isDoUpdate() {
        return mSkinPath != null;
    }


    public void restoreDefault() {
        isDefaultSkin = true;
        savePath("");
        mResources = mContext.getResources();

        notifySkinUpdate();
    }
    public void onResume(SkinUpdater updater) {
        if (!mSkinUpdaters.contains(updater)) {
            mSkinUpdaters.add(updater);
        }
    }


    public void onDestory(SkinUpdater updater) {
        if (mSkinUpdaters.contains(updater)) {
            mSkinUpdaters.remove(updater);
        }
    }

    public int getColor(SkinAttr attr) {

        // 从资源里获取 color ...
        int originColor = mContext.getResources().getColor(attr.getId());
        if(mResources == null || isDefaultSkin){
            return originColor;
        }

        String resName = mContext.getResources().getResourceEntryName(attr.getId());

        int trueResId = mResources.getIdentifier(resName, "color", mSkinPackageName);
        int trueColor = 0;

        try{
            trueColor = mResources.getColor(trueResId);
        }catch(Resources.NotFoundException e){
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }


    public Context getmContext() {
        return mContext;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

    public String getmSkinPath() {
        return mSkinPath;
    }

    public Resources getmResources() {
        return mResources;
    }

    public String getmSkinPackageName() {
        return mSkinPackageName;
    }

    interface OnSkinLoadInterface {
        void onStart();
        void onFinish(boolean success);
    }

    interface SkinUpdater{

        void apply();
    }

}
