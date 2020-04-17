package com.example.pluginproject.mvp;

public interface Example {


    interface M {
        void login(String name,String pwd);
    }

    interface VP extends M{
        void onResult(boolean success);
    }

}
