package com.example.lib;



import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AES {

    //16字节
    public static final String DEFAULT_PWD = "abcdefghijklmnop";
    //填充方式
    private static final String algorithmStr = "AES/ECB/PKCS5Padding";
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;


    static {
        init(DEFAULT_PWD);
    }

    private static void init(String password) {
        try {
            // 生成一个实现指定转换的 Cipher 对象。
            encryptCipher = Cipher.getInstance(algorithmStr);
            decryptCipher = Cipher.getInstance(algorithmStr);// algorithmStr
            byte[] keyStr = password.getBytes();
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] content) {
        try {
            byte[] result = encryptCipher.doFinal(content);
            return result;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] content) {
        try {
            byte[] result = decryptCipher.doFinal(content);
            return result;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws  Exception {

        File file = new File("proxy_tools/secret-classes.dex");

        byte[] bytes = Utils.getBytes(file);

        byte[] decrypt = decrypt(bytes);

        ByteBuffer bb = ByteBuffer.wrap(decrypt);

        FileChannel fc = new FileOutputStream("classes.dex").getChannel();
        fc.write(bb);
        fc.close();


        System.out.println(file.exists());
    }
}
