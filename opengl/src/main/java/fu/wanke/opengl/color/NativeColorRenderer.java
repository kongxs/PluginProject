package fu.wanke.opengl.color;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class NativeColorRenderer implements GLSurfaceView.Renderer {

    public NativeColorRenderer(Context context) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        surfaceCreated(Color.GRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        surfaceChanged(i,i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        onDrawFrame();
    }

    public native void surfaceCreated(int color);

    public native void surfaceChanged(int width, int height);

    public native void onDrawFrame();
}
