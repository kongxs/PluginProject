package fu.wanke.link.core;


import android.content.Context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fu.wanke.link.Constants;
import fu.wanke.link.Loger;
import fu.wanke.link.MessagePage;
import fu.wanke.link.socket.LinkSocket;
import fu.wanke.link.socket.SocketFactory;

public class MultiplexingRunnable implements Runnable{


    private static  MultiplexingRunnable instance = new MultiplexingRunnable();

    private ExecutorService mSendMsgPool;

    private LinkSocket mSocket;

    private boolean isRun = true ;

    private boolean isConnection ;


    public MultiplexingRunnable() {
        mSendMsgPool = Executors.newFixedThreadPool(10);
    }

    public static MultiplexingRunnable newInstance() {
        return instance;
    }

    // 开启连接
    public void connect() {
        Loger.get().debug("开始链接PushSocket,connect");
        mSendMsgPool.execute(instance);
    }

    public void sendMsg( MessagePage page) {
        mSendMsgPool.execute(new MessageSender(mSocket,page));
    }

    @Override
    public void run() {

        while(isRun) {

            if (isConnection) {
                break;
            }

            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mSocket = null;
            }
            mSocket = SocketFactory.get();
            mSocket.setReUsed(true);

            try{
                SocketAddress address = new InetSocketAddress(Constants.IP_LONG,Constants.PORT_LONG);
                mSocket.connection(address,30000);


                mSocket.getSocket().setKeepAlive(false);

            } catch (Exception e)  {
                Loger.get().debug(e.getMessage());
            }

            isConnection = true;

            mSocket.readMessage();
        }


    }



}
