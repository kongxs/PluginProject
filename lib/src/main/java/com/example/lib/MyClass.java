package com.example.lib;

import java.io.File;
import java.io.IOException;

public class MyClass {


    public static void main(String[] args) throws Exception {
        System.out.println("hello worrld ");

        aar2dex();

    }


    private static File aar2dex() throws IOException, InterruptedException {
        /**
         * 1.制作只包含解密代码的dex文件
         */
        File aarFile = new File("lib/proxy_core-debug.aar");


        System.out.println("aarfile  : " + aarFile
        .exists());

        System.out.println(aarFile.exists());
        File aarTemp = new File("temp");
        Zip.unZip(aarFile,aarTemp);


        File classesJar = new File(aarTemp, "classes.jar");
        File classesDex = new File(aarTemp, "classes.dex");
        if (classesJar.exists()) {
            System.out.println("class ......... ");
        }
//
//        //dx --dex --output out.dex in.jar
        String s = "dx --dex --output " + classesDex.getAbsolutePath()
                + " " + classesJar.getAbsolutePath();

        System.out.println("dx commad :"+ s);
        Process process = Runtime.getRuntime().exec(s);
        process.waitFor();
        if (process.exitValue() != 0) {
            throw new RuntimeException("dex error");
        }
        return classesDex;
    }

}
