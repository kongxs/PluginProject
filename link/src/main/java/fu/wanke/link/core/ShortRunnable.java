package fu.wanke.link.core;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import fu.wanke.link.Constants;
import fu.wanke.link.Loger;
import fu.wanke.link.MessagePage;
import fu.wanke.link.socket.LinkSocket;
import fu.wanke.link.socket.SocketFactory;

public class ShortRunnable implements Runnable {


    private final MessagePage mPage;


    public ShortRunnable(MessagePage page) {
        this.mPage = page;
    }

    @Override
    public void run() {

        LinkSocket mSocket = SocketFactory.get();

        try{
            SocketAddress address = new InetSocketAddress(Constants.IP_SHORT,Constants.PORT_SHORT);
            mSocket.connection(address,30000);
        } catch (Exception e)  {
            Loger.get().debug(e.getMessage());
        }

        new MessageSender(mSocket,mPage).run();

        new MessageReader(mSocket).run();

        mSocket.setReUsed(false);
    }
}
