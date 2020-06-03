package fu.wanke.link.dispatcher;

import java.util.concurrent.Executors;

import fu.wanke.link.core.Commands;
import fu.wanke.link.heart.HeartBeatManager;

public class MessageDispatcher {



    public static void dispatch(short command, String decrypt) {

        if (command == Commands.COMMAND_LINK_RESP) {
            // start heart
            Executors.newSingleThreadExecutor().execute(new HeartBeatManager());
        }
    }


}
