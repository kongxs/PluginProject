package com.example.pluginproject.data;

import com.example.pluginproject.Modify;

public class People {

    private String name = "k";

    @Modify
    public int getAge(int age) {
        return 10;
    }

}
