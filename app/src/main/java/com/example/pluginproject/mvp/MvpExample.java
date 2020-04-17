package com.example.pluginproject.mvp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pluginproject.R;

import fu.wanke.mvp.base.MvpActivity;

public class MvpExample extends MvpActivity<ExamplePresenter , Example.VP> {

    EditText name;
    EditText pwd;
    Button  btn;


    private Example.VP vp = new Example.VP() {
        @Override
        public void onResult(boolean success) {
            String result = success ? "成功" : "失败 ";
            Toast.makeText(MvpExample.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void login(String name, String pwd) {
            mPresenter.getContact().login(name, pwd);
        }
    };

    @Override
    public Example.VP getContact() {

        return vp;
    }

    @Override
    protected void initView() {
        name = findViewById(R.id.name);
        pwd = findViewById(R.id.pwd);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(this);
    }

    @Override
    protected ExamplePresenter getPresenter() {
        return new ExamplePresenter();
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_mvp_example;
    }

    @Override
    public void onClick(View v) {
        getContact().login(name.getText().toString() , pwd.getText().toString());
    }

}
