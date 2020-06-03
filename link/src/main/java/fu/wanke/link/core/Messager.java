package fu.wanke.link.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import fu.wanke.link.Loger;
import fu.wanke.link.utils.NumberUtil;

public class Messager {


    public static short readCommandType(InputStream in) {
        byte[] command = new byte[2];
        try {
            in.read(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NumberUtil.getShortArray(command);
    }

    // 读取数据
    public static byte[] readData(InputStream in, byte[] head) {
        // JCM端下发的消息内容字节长度
        try {
            short contentLength = (short) (NumberUtil.getShortArray(head) - 4);// 获取内容长度
            if (contentLength <= 0) {
                return null;
            }
            byte[] msgBody = new byte[contentLength];
            int readCount = 0; // 已经成功读取的字节的个数
            while (readCount < contentLength) {
                readCount += in.read(msgBody, readCount, contentLength - readCount);
            }
            return msgBody;
        } catch (UnsupportedEncodingException e) {
            Loger.get().debug( e.toString());
        } catch (Exception e) {
            Loger.get().debug( e.toString());
        }
        return null;
    }


}
