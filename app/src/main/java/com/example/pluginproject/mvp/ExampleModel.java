package com.example.pluginproject.mvp;


import fu.wanke.mvp.base.MvpModel;

public class ExampleModel extends MvpModel<ExamplePresenter , Example.M> {

    private Example.M m = new Example.M() {
        @Override
        public void login(String name, String pwd) {
            // mock net ...
            if ("wanke".equals(name) && "123".equals(pwd)) {
                mPresenter.getContact().onResult(true);
            } else {
                mPresenter.getContact().onResult(false);
            }
        }
    };

    public ExampleModel(ExamplePresenter mPresenter) {
        super(mPresenter);
    }

    @Override
    public Example.M getContact() {

        return m;
    }

}
