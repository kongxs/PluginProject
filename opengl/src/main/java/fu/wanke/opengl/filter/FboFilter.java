package fu.wanke.opengl.filter;


import android.content.Context;
import android.opengl.GLES30;

public class FboFilter extends Filter {


    private int[] mFrameBuffers = new int[1];
    private int[] mFBOTextures;

    public FboFilter(Context mContext) {

        super(mContext);
    }

    @Override
    public void resetCooridate() {

//        POSITION_VERTEX = new float[]{
//                -1, 1,
//                -1, -1,
//                1, 1,
//                1, -1
//        };

        TEX_VERTEX = new  float[]{
                0, 0,
                0,1,
                1,0,
                1,1
        };
    }

    private void loadFbo() {

        if (mFrameBuffers != null) {
            destroyFrameBuffers();
        }
        //创建FrameBuffer
        mFrameBuffers = new int[1];
        GLES30.glGenFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);
        //穿件FBO中的纹理
        mFBOTextures = new int[1];

        glGenTextures(mFBOTextures);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mFBOTextures[0]);
        //指定FBO纹理的输出图像的格式 RGBA
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, mWidth, mHeight,
                0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFrameBuffers[0]);

        //将fbo绑定到2d的纹理上
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0,
                GLES30.GL_TEXTURE_2D, mFBOTextures[0], 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

    }

    private void destroyFrameBuffers() {
        //删除fbo的纹理
        if (mFBOTextures != null) {
            GLES30.glDeleteTextures(1, mFBOTextures, 0);
            mFBOTextures = null;
        }
        //删除fbo
        if (mFrameBuffers != null) {
            GLES30.glDeleteFramebuffers(1, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
    }

    @Override
    public int draw(int textureId) {

        //锁定绘制的区域  绘制是从左下角开始的
        GLES30.glViewport(0, 0, mWidth, mHeight);
        //绑定FBO，在FBO上操作
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFrameBuffers[0]);

        //使用着色器
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

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);


        return mFBOTextures[0];
    }

    @Override
    public void prepare(int width  ,int  height) {
        super.prepare(width,height);

        loadFbo();
    }

    public static void glGenTextures(int[] textures) {
        GLES30.glGenTextures(textures.length, textures, 0);


        for (int i = 0; i < textures.length; i++) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[i]);


            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);


            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        }
    }
}
