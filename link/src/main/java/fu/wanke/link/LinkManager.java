package fu.wanke.link;


import android.content.Context;

import fu.wanke.link.connection.MultiplexingConnection;
import fu.wanke.link.connection.ShortConnection;

public class LinkManager {

    private static LinkManager mInstance = null;

    private Context mContext;

    private boolean mSSL = true;


    public LinkManager() {
    }


    public static LinkManager getInstance() {
        if (mInstance == null) {
            mInstance = new LinkManager();
        }
        return mInstance;
    }

    public  LinkManager register() {

        ShortConnection.newInstance().pushMessage(MessagePage.mockRegister());

        return this;
    }


    public LinkManager connection() {
        MultiplexingConnection.connect();
        return this;
    }

    public LinkManager sendMessage(MessagePage page) {
        MultiplexingConnection.sendMsg(page);
        return this;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public boolean ismSSL() {
        return mSSL;
    }

    public LinkManager setmSSL(boolean mSSL) {
        this.mSSL = mSSL;
        return this;
    }
}
