package fu.wanke.opengl.render;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES30;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ColorRender implements GLSurfaceView.Renderer {

    private final Context mContext;
    int color = Color.GRAY;

    public ColorRender(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //设置背景颜色
        float redF = (float) Color.red(color) / 255;
        float greenF = (float) Color.green(color) / 255;
        float blueF = (float) Color.blue(color) / 255;
        float alphaF = (float) Color.alpha(color) / 255;
        GLES30.glClearColor(redF, greenF, blueF, alphaF);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }
}
