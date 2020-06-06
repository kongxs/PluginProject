package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.filter.FboFilter;
import fu.wanke.opengl.filter.Shape_FBO;

public class FBORender extends BaseRender {

    public static int sScreenWidth;

    public static int sScreenHeight;

    private Shape_FBO mRectangle;
    private FboFilter filter;

    public FBORender(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1);

        mRectangle = new Shape_FBO(mContext);
        filter = new FboFilter(mContext);

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        sScreenWidth = width;

        sScreenHeight = height;

        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        int draw = filter.draw();


        mRectangle.draw(draw);
    }
}
