package fu.wanke.link;

import android.util.Log;

public class Loger {

    private static final Loger LOGER =  new Loger();

    private static final String TAG = "Loger";

    public static Loger get() {
        return LOGER;
    }


    public void debug(String tag , String message) {
        Log.e(tag,message);
    }


    public void debug( String message) {
        debug(TAG,message);
    }

}
