package com.example.pluginproject;

public class TestLog {

    public TestLog(String cons_invoked_) {
        log(cons_invoked_ ,cons_invoked_);
    }

    public  void  log(String msg , String message2) {
        System.out.println("TestLog : " + msg + " , " + message2);
    }
}
