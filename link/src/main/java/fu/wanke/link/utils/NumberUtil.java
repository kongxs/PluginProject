package fu.wanke.link.utils;

public class NumberUtil {

    /**
     * 获取int的 4位 byte数组
     *
     * @param len
     * @return
     */
    public static byte[] get4ByteArray(int len) {
        byte[] head = new byte[4];
        head[0] = (byte) ((len >> 24) & 0xFF);
        head[1] = (byte) ((len >> 16) & 0xFF);
        head[2] = (byte) ((len >> 8) & 0xFF);
        head[3] = (byte) (len & 0xFF);
        return head;
    }

    /**
     * 获取int的 2位 byte数组
     *
     * @param len
     * @return
     */
    public static byte[] get2ByteArray(short len) {
        byte[] head = new byte[2];
        head[0] = (byte) ((len >> 8) & 0xFF);
        head[1] = (byte) (len & 0xFF);
        return head;
    }

    /**
     * 获取byte数组的short值
     *
     * @param b
     * @return
     */
    public static short getShortArray(byte[] b) {
        if (b == null) {
            return 0;
        }
        return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
    }

    /**
     * 获取byte数组的int值
     *
     * @param b
     * @return
     */
    public static int getIntArray(byte[] b) {
        if (b == null) {
            return 0;
        }
        return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
    }

}
