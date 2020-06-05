package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class BaseRender implements GLSurfaceView.Renderer {

    protected Context mContext;

    public BaseRender(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0,0,width,height);
    }



    protected FloatBuffer toBuffer(float[] vertexPoints) {
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertexPoints);
        vertexBuffer.position(0);

        return vertexBuffer;
    }

    protected ShortBuffer toBuffer(short[] vertexPoints) {
        ShortBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        vertexBuffer.put(vertexPoints);
        vertexBuffer.position(0);

        return vertexBuffer;
    }
}
