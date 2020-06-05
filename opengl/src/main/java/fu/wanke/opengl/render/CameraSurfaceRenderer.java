package fu.wanke.opengl.render;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.ShaderUtils;
import fu.wanke.opengl.texture.Camera1HelperTexture;

public class CameraSurfaceRenderer extends BaseRender {


    private static final String TAG = "TextureRenderer";

    private FloatBuffer vertexBuffer, mTexVertexBuffer;

    private ShortBuffer mVertexIndexBuffer;

    private int mProgram;


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
            1f, 1f,     //纹理坐标V1
            0f, 1f,     //纹理坐标V2
            0f, 0.0f,   //纹理坐标V3
            1f, 0.0f    //纹理坐标V4
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

    private float[] transformMatrix = new float[16];

    /**
     * 渲染容器
     */
    private GLSurfaceView mGLSurfaceView;

    /**
     * 矩阵索引
     */
    private int uTextureMatrixLocation;

    private int uTextureSamplerLocation;

    private String vertexShaderId =
            "#version 300 es\n" +
                    "layout (location = 0) in vec4 vPosition;\n" +
                    "layout (location = 1) in vec4 aTextureCoord;\n" +
                    "//纹理矩阵\n" +
                    "uniform mat4 uTextureMatrix;\n" +
                    "out vec2 yuvTexCoords;\n" +
                    "void main() {\n" +
                    "     gl_Position  = vPosition;\n" +
                    "     gl_PointSize = 10.0;\n" +
                    "     //只保留x和y分量\n" +
                    "     yuvTexCoords = (uTextureMatrix * aTextureCoord).xy;\n" +
                    "}";

    private String fragmentShaderId =
            "#version 300 es\n" +
                    "//OpenGL ES3.0外部纹理扩展\n" +
                    "#extension GL_OES_EGL_image_external_essl3 : require\n" +
                    "precision mediump float;\n" +
                    "uniform samplerExternalOES yuvTexSampler;\n" +
                    "in vec2 yuvTexCoords;\n" +
                    "out vec4 vFragColor;\n" +
                    "void main() {\n" +
                    "     vFragColor = texture(yuvTexSampler,yuvTexCoords);\n" +
                    "}";
    private SurfaceTexture mSurfaceTexture;
    private int textureId;

    public CameraSurfaceRenderer(Context context , GLSurfaceView surfaceView) {
        super(context);

        this.mGLSurfaceView = surfaceView;

        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = toBuffer(POSITION_VERTEX);
        //传入指定的坐标数据

        mTexVertexBuffer = toBuffer(TEX_VERTEX);

        mVertexIndexBuffer = toBuffer(VERTEX_INDEX);
    }


    /**
     * 加载外部纹理
     * @return
     */
    public int loadTexture() {
        int[] tex = new int[1];
        //创建一个纹理
        GLES30.glGenTextures(1, tex, 0);
        //绑定到外部纹理上
//        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
//        //设置纹理过滤参数
//        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
//        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
//        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
//        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
//        //解除纹理绑定
//        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //编译
        //链接程序片段
        mProgram = ShaderUtils.loaderShader(vertexShaderId, fragmentShaderId);

        uTextureMatrixLocation = GLES30.glGetUniformLocation(mProgram, "uTextureMatrix");
        //获取Shader中定义的变量在program中的位置
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mProgram, "yuvTexSampler");

        loadSurfaceTexture();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        //使用程序片段
        GLES30.glUseProgram(mProgram);

        //更新纹理图像
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(transformMatrix);

        //激活纹理单元0
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //绑定外部纹理到纹理单元0
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        //将此纹理单元床位片段着色器的uTextureSampler外部纹理采样器
        GLES30.glUniform1i(uTextureSamplerLocation, 0);

        //将纹理矩阵传给片段着色器
        GLES30.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer);

        // 绘制
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, VERTEX_INDEX.length, GLES30.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

    }


    /**
     * 创建一个 纹理，并绑定到离屏buffer
     * @return
     */
    public boolean loadSurfaceTexture() {

        int[] tex = new int[1];
        //创建一个纹理
        GLES30.glGenTextures(1, tex, 0);

        textureId = tex[0];

        //根据纹理ID创建SurfaceTexture
        Camera1HelperTexture helperTexture = new Camera1HelperTexture(mContext);


        mSurfaceTexture = new SurfaceTexture(textureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mGLSurfaceView.requestRender();
            }
        });

        helperTexture.open(mSurfaceTexture, false);

        return true;
    }
}
