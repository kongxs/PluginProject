package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.ShaderUtils;

/*
   绘制矩形
 */
public class TriangleRender extends BaseRender {


    private String vertex_shader = "#version 300 es\n" +
            " layout (location = 0) in vec4 vPosition;\n" +
            " void main() {\n" +
            "      gl_Position  = vPosition;\n" +
            "      gl_PointSize = 10.0;\n" +
            " }";

    private String fragment_shader = "#version 300 es\n" +
            " precision mediump float;\n" +
            " out vec4 fragColor;\n" +
            " void main() {\n" +
            "      fragColor = vec4(1.0,1.0,1.0,1.0);\n" +
            " }";

    FloatBuffer vertexBuffer ;

//    private float[] vertexPoints = new float[]{
//            // 第一个三角形
//            0.5f, 0.5f, 0.0f,   // 右上角
//            0.5f, -0.5f, 0.0f,  // 右下角
//            -0.5f, 0.5f, 0.0f,  // 左上角
//            // 第二个三角形
//            0.5f, -0.5f, 0.0f,  // 右下角
//            -0.5f, -0.5f, 0.0f, // 左下角
//            -0.5f, 0.5f, 0.0f   // 左上角
//    };

    private float[] vertexPoints = new float[]{
            // 第一个三角形
            -0.5f, 0.5f, 0.0f,   // 右上角
            -0.5f, -0.5f, 0.0f,  // 右下角
            0.5f, -0.5f, 0.0f,  // 左上角
            // 第二个三角形
            0.5f, 0.5f, 0.0f

    };


    public TriangleRender(Context context) {

        super(context);

        vertexBuffer = toBuffer(vertexPoints);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.5f,0.5f,0.5f,0.5f);

        int mProgram = ShaderUtils.loaderShader(vertex_shader, fragment_shader);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
//启用顶点的句柄
        GLES30.glEnableVertexAttribArray(0);
//绘制三个点
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 4);


//禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0);
    }
}
