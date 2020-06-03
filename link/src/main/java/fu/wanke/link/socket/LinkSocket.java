package fu.wanke.link.socket;


import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import fu.wanke.link.LinkException;
import fu.wanke.link.Loger;
import fu.wanke.link.MessagePage;
import fu.wanke.link.core.Messager;
import fu.wanke.link.dispatcher.MessageDispatcher;
import fu.wanke.link.utils.NumberUtil;


public abstract class LinkSocket {


    protected java.net.Socket mSocket = null;

    private boolean reUsed = true;

    public LinkSocket() {
        mSocket = createSocket();
    }

    protected abstract java.net.Socket createSocket();

    public abstract void connection(SocketAddress address , long timeOutTime) throws Exception;


    public Socket getSocket() {
        return mSocket;
    }

    public boolean isReUsed() {
        return reUsed;
    }

    public void setReUsed(boolean reUsed) {
        this.reUsed = reUsed;
    }

    //    public void startRead() {
//
//    }

    public void readMessage() {
        try {

            InputStream in = mSocket.getInputStream();


            byte[] head = new byte[2]; // 读取头信息 用来标识下发内容的长度


            while(isReUsed()) {

                int len = in.read(head, 0, 2);// 读取头部


                if (len == -1) {
                    return;
                }


                short command = Messager.readCommandType(in);

                byte[] msgBody = Messager.readData(in, head);

                String decrypt = "";
                if (msgBody != null && msgBody.length > 0) {
                    decrypt = new String(msgBody, "UTF-8");
                }

                Loger.get().debug(String.format("收到应答 command：%s,data：%s，time：%s",
                        command, decrypt, System.currentTimeMillis()));

                MessageDispatcher.dispatch(command ,decrypt );
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != mSocket && !isReUsed()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendMessage(MessagePage page) {

        Loger.get().debug("send message : " + page.toString());

        OutputStream out = null;
        try {
            out = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (page == null) {
            return;
        }

        byte[] total;

        String msgBody = page.getMsgBody();

        if (TextUtils.isEmpty(msgBody)) {
            msgBody = "";
        }

        if (!TextUtils.isEmpty(msgBody)) {
            byte[] len = NumberUtil.get2ByteArray((short) (msgBody.getBytes().length + 4));
            byte[] command = NumberUtil.get2ByteArray(page.getCommand());
            byte[] body = msgBody.getBytes();
            total = new byte[4 + body.length];
            System.arraycopy(len, 0, total, 0, len.length);
            System.arraycopy(command, 0, total, 2, command.length);
            System.arraycopy(body, 0, total, 4, body.length);
        } else {
            byte[] len = NumberUtil.get2ByteArray((short) (4));
            byte[] command = NumberUtil.get2ByteArray(page.getCommand());
            total = new byte[4];
            System.arraycopy(len, 0, total, 0, len.length);
            System.arraycopy(command, 0, total, 2, command.length);
        }
        if (out != null) {
            try {
                out.write(total);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                throw new LinkException(e.getMessage());
            }
        }
        Loger.get().debug(String.format("发送请求 command：%s,data：%s，time：%s",
                page.getCommand(), msgBody, System.currentTimeMillis()));
    }


    public void close() throws IOException {
        if (mSocket != null) {
            mSocket.close();
        }
    }
}
