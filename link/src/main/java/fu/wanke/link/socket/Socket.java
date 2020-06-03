package fu.wanke.link.socket;

import java.io.IOException;
import java.net.SocketAddress;

import fu.wanke.link.MessagePage;


public class Socket extends LinkSocket {


    @Override
    protected java.net.Socket createSocket() {
        return new java.net.Socket();
    }

    @Override
    public void connection(SocketAddress address, long timeOutTime) throws IOException {
        mSocket.connect(address);
    }

    @Override
    public void sendMessage(MessagePage message) {
        super.sendMessage(message);
    }
}
