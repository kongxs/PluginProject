package fu.wanke.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class MvpActivity<P extends MvpPresenter , CONTACT> extends AppCompatActivity
        implements View.OnClickListener ,MvpContact<CONTACT> {

    protected P mPresenter ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(getLayoutId());


        mPresenter = getPresenter();
        mPresenter.bindView(this);

        initView();
        initData();
    }


    protected abstract void initView();

    protected abstract P getPresenter();

    protected abstract void initData();

    protected abstract int getLayoutId();


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unBindView();
    }
}
