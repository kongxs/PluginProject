package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.R;
import fu.wanke.opengl.ShaderUtils;
import fu.wanke.opengl.TexureUtils;

public class BitmapRender extends BaseRender {

    String vertex_shader = "#version 300 es\n" +
            "layout (location = 0) in vec4 vPosition;\n" +
            "layout (location = 1) in vec2 aTextureCoord;\n" +
            "//输出纹理坐标(s,t)\n" +
            "out vec2 vTexCoord;\n" +
            "void main() {\n" +
            "     gl_Position  = vPosition;\n" +
            "     gl_PointSize = 10.0;\n" +
            "     vTexCoord = aTextureCoord;\n" +
            "}";

    String fragment_shader = "#version 300 es\n" +
            "precision mediump float;\n" +
            "uniform sampler2D uTextureUnit;\n" +
            "//接收刚才顶点着色器传入的纹理坐标(s,t)\n" +
            "in vec2 vTexCoord;\n" +
            "out vec4 vFragColor;\n" +
            "void main() {\n" +
            "     vFragColor = texture(uTextureUnit,vTexCoord);\n" +
            "}";

    private static final String TAG = "TextureRenderer";

    private FloatBuffer vertexBuffer, mTexVertexBuffer;

    private ShortBuffer mVertexIndexBuffer;

    private int mProgram;

    private int textureId;

    /**
     * 顶点坐标
     * (x,y,z)
     */
    private float[] POSITION_VERTEX = new float[]{
            0f, 0f, 0f,     //顶点坐标V0
            1f, 1f, 0f,     //顶点坐标V1
            -1f, 1f, 0f,    //顶点坐标V2
            -1f, -1f, 0f,   //顶点坐标V3
            1f, -1f, 0f     //顶点坐标V4
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

    /**
     * 索引
     */
    private static final short[] VERTEX_INDEX = {
            0, 1, 2,  //V0,V1,V2 三个顶点组成一个三角形
            0, 2, 3,  //V0,V2,V3 三个顶点组成一个三角形
            0, 3, 4,  //V0,V3,V4 三个顶点组成一个三角形
            0, 4, 1   //V0,V4,V1 三个顶点组成一个三角形
    };

    public BitmapRender(Context context) {
        super(context);


        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(POSITION_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //传入指定的坐标数据
        vertexBuffer.put(POSITION_VERTEX);
        vertexBuffer.position(0);

        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
        mTexVertexBuffer.position(0);

        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(VERTEX_INDEX);
        mVertexIndexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        mProgram = ShaderUtils.loaderShader(vertex_shader, fragment_shader);

        //加载纹理
        textureId = TexureUtils.loadTexture(mContext, R.drawable.mm);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        //使用程序片段
        GLES30.glUseProgram(mProgram);
        //启用顶点坐标属性
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        //启用纹理坐标属性
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer);
        //激活纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);

        // 绘制
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, VERTEX_INDEX.length, GLES30.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

    }
}
