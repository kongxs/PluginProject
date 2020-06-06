package fu.wanke.opengl.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.nio.FloatBuffer;

import fu.wanke.opengl.R;
import fu.wanke.opengl.ShaderUtils;
import fu.wanke.opengl.render.BaseRender;
import fu.wanke.opengl.render.FBORender;

public class FboFilter {

    private final Context mContext;
    private final FloatBuffer mSqureBuffer;
    private int mFrameBufferProgram;

    int mLoadedTextureId ;

    private String vertexShaderCode = ""

            + "attribute vec2 aPosition;"

            + "attribute vec2 aTextureCoord;"

            + "varying vec2 vTextureCoord;"

            + "void main(){"

            + "gl_Position = vec4(aPosition,0,1);"

            + "vTextureCoord = aTextureCoord;"

            + "}";

    private String fragmentShaderCode = "precision mediump float;"

            + "uniform sampler2D uTexture;"

            + "varying vec2 vTextureCoord;"

            + "void main(){"

            + "gl_FragColor = texture2D(uTexture, vTextureCoord);"

            + "}";

    public FboFilter(Context context) {
        this.mContext = context;

        mLoadedTextureId = initTexture(R.drawable.mm);

        float [] bgVertex = new float[] {

                -1f,-1f,

                -1f,1f,

                1f,-1f,

                1f,1f,

        };


        mSqureBuffer  = BaseRender.toBuffer(bgVertex);

    }

    public int initTexture(int res) {

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);

        int [] textures = new int[1];

        GLES30.glGenTextures(1, textures, 0);

        //绑定纹理缓存到纹理单元

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);

        //设置采样，拉伸方式

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_MIRRORED_REPEAT);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_MIRRORED_REPEAT);

        //指定纹理图片生成2D纹理

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        //释放bitmap

        bitmap.recycle();

        //解除绑定

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return textures[0];

    }

    public int  draw() {
        // 生成FrameBuffer

        int [] framebuffers = new int[1];

        GLES30.glGenFramebuffers(1, framebuffers, 0);

        // 生成Texture

        int [] textures = new int[2];

        GLES30.glGenTextures(2, textures, 0);

        int colorTxtureId = textures[0];

        //绑定纹理缓存到纹理单元

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, colorTxtureId);

        //设置采样，拉伸方式

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_MIRRORED_REPEAT);

        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_MIRRORED_REPEAT);

        //生成2D纹理

        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, FBORender.sScreenWidth, FBORender.sScreenHeight,0, GLES30.GL_RGB, GLES30.GL_UNSIGNED_SHORT_5_6_5, null);

        //绑定framebuffer

        int framebufferId = framebuffers[0];

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, framebufferId);

        //挂载textureID到framebufferId

        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, colorTxtureId, 0);

        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER)== GLES30.GL_FRAMEBUFFER_COMPLETE) {
        }


        mFrameBufferProgram = ShaderUtils.loaderShader(vertexShaderCode , fragmentShaderCode);

        int positionHandle1 = GLES30.glGetAttribLocation(mFrameBufferProgram, "aPosition");

        int textureCoordHandle1 = GLES30.glGetAttribLocation(mFrameBufferProgram, "aTextureCoord");

        int textureHandle1 = GLES30.glGetUniformLocation(mFrameBufferProgram, "uTexture");


        GLES30.glVertexAttribPointer(positionHandle1, 2, GLES30.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);


        GLES30.glVertexAttribPointer(textureCoordHandle1, 2, GLES30.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);

        GLES30.glEnableVertexAttribArray(positionHandle1);

        GLES30.glEnableVertexAttribArray(textureCoordHandle1);

//        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mLoadedTextureId);

        GLES30.glUniform1i(textureHandle1, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0);


        return textures[0];
    }
}
