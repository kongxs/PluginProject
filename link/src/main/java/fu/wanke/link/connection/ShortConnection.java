package fu.wanke.link.connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fu.wanke.link.MessagePage;
import fu.wanke.link.core.ShortRunnable;


public class ShortConnection {


    private  ExecutorService mThreadPool;


    private static ShortConnection mConnection = new ShortConnection();


    public ShortConnection() {
        mThreadPool = Executors.newSingleThreadExecutor();
    }

    public static ShortConnection newInstance(){
        return mConnection;
    }

    public void pushMessage(MessagePage page) {
        mThreadPool.execute(new ShortRunnable(page));
    }
}
