package com.example.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class Utils {

    /**
     * 读取文件
     * @param file
     * @return
     * @throws
     */
    public static byte[] getBytes(File file) throws Exception {
        RandomAccessFile r = new RandomAccessFile(file, "r");
        byte[] buffer = new byte[(int) r.length()];
        r.readFully(buffer);
        r.close();
        return buffer;
    }


    public static void createFile(File file ,byte[] bytes) throws Exception {

        ByteBuffer bb = ByteBuffer.wrap(bytes);

        FileChannel fc = new FileOutputStream(file.getAbsolutePath()).getChannel();
        fc.write(bb);
        fc.close();
    }


}
