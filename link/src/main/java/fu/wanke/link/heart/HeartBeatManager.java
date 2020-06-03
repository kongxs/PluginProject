package fu.wanke.link.heart;

import fu.wanke.link.MessagePage;
import fu.wanke.link.connection.MultiplexingConnection;

public class HeartBeatManager implements Runnable {


    @Override
    public void run() {
        while(true) {

            MultiplexingConnection.sendMsg(MessagePage.mockHeart());


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
