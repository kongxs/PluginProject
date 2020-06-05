package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.ShaderUtils;

/**
 * @anchor: andy
 * @date: 2018-11-02
 * @description: 彩色三角形
 */
public class SimpleColorRenderer extends BaseRender {

    String vertex_shader = "#version 300 es\n" +
            "layout (location = 1) in vec4 vPosition;\n" +
            "layout (location = 0) in vec4 aColor;\n" +
            "out vec4 vColor;\n" +
            "void main() {\n" +
            "     gl_Position  = vPosition;\n" +
            "     gl_PointSize = 10.0;\n" +
            "     vColor = aColor;\n" +
            "}" ;

    String fragment_shader = "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec4 vColor;\n" +
            "out vec4 fragColor;\n" +
            "void main() {\n" +
            "     fragColor = vColor;\n" +
            "}";

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;



    /**
     * 点的坐标
     */
    private float[] vertexPoints = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
    };

    private float color[] = new float[] {
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    public SimpleColorRenderer(Context context) {
        super(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertexPoints);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length* 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.5f,0.5f,0.5f,0.5f);

        ShaderUtils.loaderShader(vertex_shader , fragment_shader);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        // 准备坐标数据
        GLES30.glVertexAttribPointer(1 , 3 ,GLES30.GL_FLOAT , false,0,vertexBuffer);
        GLES30.glEnableVertexAttribArray(1);

        //绘制三角形颜色
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);


        GLES30.glDrawArrays(GLES30.GL_TRIANGLES , 0 , 3);

    }
}
