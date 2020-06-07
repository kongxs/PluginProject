package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.filter.Shape_FBO;

public class FBORender extends BaseRender {

    public static int sScreenWidth;

    public static int sScreenHeight;

    private Shape_FBO mRectangle;

    private Context mContext;

    public FBORender(Context context) {

        super(context);

        mContext = context;
    }





    @Override

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1);

        mRectangle = new Shape_FBO(mContext);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }

    @Override

    public void onSurfaceChanged(GL10 gl, int width, int height) {

        sScreenWidth = width;

        sScreenHeight = height;

        GLES20.glViewport(0, 0, width, height);
    }

    @Override

    public void onDrawFrame(GL10 gl) {
        mRectangle.draw();
    }
}
