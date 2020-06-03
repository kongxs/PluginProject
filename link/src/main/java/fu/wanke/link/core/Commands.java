package fu.wanke.link.core;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    public static short COMMAND_REGISTER = 1001;
    public static short COMMAND_REGISTER_RESP = 1002;



    public static short COMMAND_LINK = 2001;
    public static short COMMAND_LINK_RESP = 2002;


    public static short COMMAND_HEART = 2003;
    public static short COMMAND_HEART_RESP = 2004;


    public static short COMMAND_RECEIVER = 2005;
    public static short COMMAND_RECEIVER_REPORT = 2006;

    private static List<Short> commands = new ArrayList<>();

    static {

        commands.add(COMMAND_REGISTER);
        commands.add(COMMAND_REGISTER_RESP);

        commands.add(COMMAND_LINK);
        commands.add(COMMAND_LINK_RESP);

        commands.add(COMMAND_HEART);
        commands.add(COMMAND_HEART_RESP);

        commands.add(COMMAND_RECEIVER);
        commands.add(COMMAND_RECEIVER_REPORT);

    }


    public boolean isValidate(short command) {
        return commands.contains(command);
    }


}
