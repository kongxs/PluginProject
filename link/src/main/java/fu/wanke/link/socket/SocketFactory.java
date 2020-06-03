package fu.wanke.link.socket;

import fu.wanke.link.LinkManager;

public class SocketFactory {


    public static LinkSocket get() {

        LinkSocket socket = null;

        if (LinkManager.getInstance().ismSSL()) {
            socket = new SSLSocket();
        }
         else {
            socket = new Socket();
        }
        return socket ;
    }




}
