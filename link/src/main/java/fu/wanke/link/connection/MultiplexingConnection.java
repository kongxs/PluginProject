package fu.wanke.link.connection;



import fu.wanke.link.Loger;
import fu.wanke.link.MessagePage;
import fu.wanke.link.core.MultiplexingRunnable;

public class MultiplexingConnection {


    // 开启连接
    public static void connect() {
        Loger.get().debug("开始链接PushSocket,connect");
        MultiplexingRunnable.newInstance().connect();
    }



    public static void sendMsg(MessagePage page) {
        MultiplexingRunnable.newInstance().sendMsg(page);
    }



}
