package fu.wanke.opengl.filter;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import fu.wanke.opengl.ShaderUtils;

public class Filter {

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

//    String fragment_shader = "#version 300 es\n" +
//            "precision mediump float;\n" +
//            "uniform sampler2D uTextureUnit;\n" +
//            "//接收刚才顶点着色器传入的纹理坐标(s,t)\n" +
//            "in vec2 vTexCoord;\n" +
//            "out vec4 vFragColor;\n" +
//            "void main() {\n" +
//            "vec2 uv = vTexCoord.xy;\n" +
//            "    float y;\n" +
//            "    // 0.0～0.5 范围内显示0.25～0.75范围的像素\n" +
//            "    if (uv.y >= 0.0 && uv.y <= 0.5) {\n" +
//            "        y = uv.y + 0.25;\n" +
//            "    }else {\n" +
//            "        // 0.5～1.0范围内显示 0.25～0.75范围的像素\n" +
//            "        y = uv.y - 0.25;\n" +
//            "    }"+
//            "     vFragColor = texture(uTextureUnit,vec2(vTexCoord.x,y));\n" +
//            "}";

    private static final String TAG = "TextureRenderer";

    protected FloatBuffer vertexBuffer, mTexVertexBuffer;

//    private ShortBuffer mVertexIndexBuffer;

    protected int mProgram;

    /**
     * 顶点坐标
     * (x,y,z)
     */
    protected float[] POSITION_VERTEX = new float[]{
            -1, -1,
            -1, 1,
            1, -1,
            1, 1
    };

    /**
     * 纹理坐标
     * (s,t)
     */
    protected float[] TEX_VERTEX = {
            0, 1,
            0,0,
            1,1,
            1,0
    };

    protected Context mContext;
    protected int mWidth;
    protected int mHeight;

    public Filter(Context mContext) {
        this.mContext = mContext;
        resetCooridate();
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
    }

    public void resetCooridate() {

    }

    public void prepare(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;

        mProgram = ShaderUtils.loaderShader(vertex_shader, fragment_shader);

        //加载纹理
    }

    public int draw(int textureId) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glViewport(0, 0, mWidth, mHeight);
        //使用程序片段
        GLES30.glUseProgram(mProgram);
        //启用顶点坐标属性
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        //启用纹理坐标属性
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer);
        //激活纹理
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

        return textureId;
    }
}
