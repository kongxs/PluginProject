package com.example.pluginproject.mvp;

import fu.wanke.mvp.base.MvpPresenter;

public class ExamplePresenter
        extends MvpPresenter<MvpExample , ExampleModel , Example.VP>{


    Example.VP vp = new Example.VP() {
        @Override
        public void onResult(boolean success) {
            getView().getContact().onResult(success);
        }

        @Override
        public void login(String name, String pwd) {
            mModel.getContact().login(name, pwd);
        }
    };



    @Override
    protected ExampleModel getModel() {
        return new ExampleModel(this);
    }

    @Override
    public Example.VP getContact() {
        return vp;
    }
}
