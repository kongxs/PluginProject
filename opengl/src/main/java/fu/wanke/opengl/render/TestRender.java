package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.R;
import fu.wanke.opengl.ShaderUtils;
import fu.wanke.opengl.TexureUtils;

public class TestRender extends BaseRender {


    String vertex_shader =
            "#version 300 es\n" +
                    "layout (location=0) in vec4 vPos;\n" +
                    "layout (location = 1) in vec2 aTextureCoord;\n" +
                    "//输出纹理坐标(s,t)\n" +
                    "out vec2 vTexCoord;\n" +
                    "void main(){\n" +
                    "    gl_Position = vPos;\n" +
                    "    gl_PointSize=10.0;\n" +
                    "    vTexCoord = aTextureCoord;\n" +
                    "}";

    String fragment_shader =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "uniform sampler2D uTextureUnit;\n" +
                    "//接收刚才顶点着色器传入的纹理坐标(s,t)\n" +
                    "in vec2 vTexCoord;\n" +
                    "out vec4 vFragColor;\n" +
                    "void main(){\n" +
                    "    fragColor= texture2D(uTextureUnit,vTexCoord);\n" +
                    "}";

    float[] vertex_point = new float[]  {
            0f, 0f, 0f,     //顶点坐标V0
            1f, 1f, 0f,     //顶点坐标V1
            -1f, 1f, 0f,    //顶点坐标V2
            -1f, -1f, 0f,   //顶点坐标V3
            1f, -1f, 0f     //顶点坐标V4
    };


    float[] color_texture = new float[]{
      0.5f,0.5f,0.5f,1.0f
    };

    /**
     * 纹理坐标
     * (s,t)
     */
    private static final float[] TEX_VERTEX = {
            0.5f, 0.5f, //纹理坐标V0
            1f, 0f,     //纹理坐标V1
            0f, 0f,     //纹理坐标V2
            0f, 1.0f,   //纹理坐标V3
            1f, 1.0f    //纹理坐标V4
    };


    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;


    private int programId;
    private int textureId;


    public TestRender(Context context) {
        super(context);

        vertexBuffer = toBuffer(vertex_point);
        colorBuffer = toBuffer(color_texture);
        textureBuffer = toBuffer(TEX_VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.5f,0.5f,0.5f,1.0f);
        programId = ShaderUtils.loaderShader(vertex_shader, fragment_shader);

        int unif = GLES30.glGetUniformLocation(programId, "color");

        GLES30.glUniform4f(unif,0.5f,0f,0f,1f);

        textureId = TexureUtils.loadTexture(mContext, R.drawable.mm);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);


        GLES30.glVertexAttribPointer(0 ,3 ,GLES30.GL_FLOAT ,false,0 , vertexBuffer);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glVertexAttribPointer(1 ,3 ,GLES30.GL_FLOAT ,false,0 , textureBuffer);
        GLES30.glEnableVertexAttribArray(1);

//        //绘制三角形颜色
//        GLES30.glEnableVertexAttribArray(1);
//        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);


        GLES30.glActiveTexture(textureId);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D , textureId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP ,0 , 6);

//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES ,0 , 6);

        GLES30.glDisableVertexAttribArray(0);

    }
}
