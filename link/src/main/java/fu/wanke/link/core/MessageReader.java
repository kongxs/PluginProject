package fu.wanke.link.core;


import fu.wanke.link.socket.LinkSocket;

public class MessageReader implements Runnable {

    private LinkSocket mSocket;

    public MessageReader(LinkSocket mSocket) {
        this.mSocket = mSocket;
    }

    @Override
    public void run() {
        mSocket.readMessage();
    }
}
