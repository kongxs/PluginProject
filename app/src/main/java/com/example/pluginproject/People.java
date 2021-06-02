package com.example.pluginproject;

import com.example.pluginproject.Modify;

public class People {

    private String name = "k";

    @Modify
    public int getAge(int age) {
        return 10;
    }

}
