package fu.wanke.mvp.base;

import java.lang.ref.WeakReference;

public abstract class MvpPresenter<V extends MvpActivity ,
        M extends MvpModel , CONTACT> implements MvpContact<CONTACT> {


    protected M mModel;

    private WeakReference<V> reference;

    public MvpPresenter() {
        mModel = getModel();
    }


    protected abstract M getModel();


    void bindView(V view) {

        if (view != null) {
            reference = new WeakReference<>(view);
        }
    }

    void unBindView() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }

    protected V getView() {
        return reference ==  null ? null : reference.get();
    }

}
