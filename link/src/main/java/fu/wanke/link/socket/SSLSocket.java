package fu.wanke.link.socket;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import fu.wanke.link.LinkException;
import fu.wanke.link.Loger;
import fu.wanke.link.MessagePage;
import fu.wanke.link.ssl.SSLSocketAssistant;
import fu.wanke.link.ssl.SSLSocketException;
import fu.wanke.link.utils.NumberUtil;

public class SSLSocket extends LinkSocket {


    @Override
    protected javax.net.ssl.SSLSocket createSocket() {
        try {
            return SSLSocketAssistant.createSSLSocket();
        } catch (SSLSocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void connection(SocketAddress address, long timeOutTime) throws Exception {
        SSLSocketAssistant.connect((javax.net.ssl.SSLSocket) mSocket, address,
                (int) timeOutTime, ((InetSocketAddress)address).getHostName());
    }

    @Override
    public void sendMessage(MessagePage page) {
        super.sendMessage(page);
    }


}
