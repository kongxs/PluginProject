package com.example.pluginproject;

public class PeoRedirect implements  ChangeQuickRedirect {

    @Override
    public Object accessDispatch(Object[] methodParams, Object originObj, boolean isStatic, int methodNumber) {


        System.out.println("PeopleRedirect = " + methodParams);
        System.out.println("PeopleRedirect = " + originObj);
        System.out.println("PeopleRedirect = " + isStatic);
        System.out.println("PeopleRedirect = " + methodNumber);

        return 100;
    }

    @Override
    public boolean isSupport(Object[] methodParams, Object originObj, boolean isStatic, int methodNumber) {
        return true;
    }

}
