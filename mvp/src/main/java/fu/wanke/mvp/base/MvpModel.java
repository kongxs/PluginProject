package fu.wanke.mvp.base;

public abstract class MvpModel<P extends MvpPresenter , CONTACT>
        implements MvpContact<CONTACT> {

    protected P  mPresenter ;

    public MvpModel(P mPresenter) {
        this.mPresenter = mPresenter;
    }

}
