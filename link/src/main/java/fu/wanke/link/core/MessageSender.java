package fu.wanke.link.core;

import fu.wanke.link.MessagePage;
import fu.wanke.link.socket.LinkSocket;

public class MessageSender implements Runnable {

    private final MessagePage mPage;
    public LinkSocket mSocket ;

    public MessageSender(LinkSocket mSocket, MessagePage page) {
        this.mSocket = mSocket;
        this.mPage = page;
    }

    @Override
    public void run() {
        mSocket.sendMessage(mPage);
    }
}
