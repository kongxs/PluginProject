package fu.wanke.opengl.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import fu.wanke.opengl.R;
import fu.wanke.opengl.TexureUtils;

public class WaterFilter extends FboFilter {

    private final Bitmap mBitmap;
    private int mTextureId[];

    public WaterFilter(Context mContext) {
        super(mContext);

        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);

    }

    @Override
    public void prepare(int width, int height) {
        super.prepare(width, height);

        mTextureId = new int[1];

        glGenTextures(mTextureId);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId[0]);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public int draw(int textureId) {

        int draw = super.draw(textureId);

        drawWater();

        return mFBOTextures[0];
    }

    private void drawWater() {
        //开启混合模式
        GLES30.glEnable(GLES30.GL_BLEND);
        //设置贴图模式
        // 1：src 源图因子 ： 要画的是源  (耳朵)
        // 2: dst : 已经画好的是目标  (从其他filter来的图像)
        //画耳朵的时候  GL_ONE:就直接使用耳朵的所有像素 原本是什么样子 我就画什么样子
        // 表示用1.0减去源颜色的alpha值来作为因子
        //  耳朵不透明 (0,0 （全透明）- 1.0（不透明）) 目标图对应位置的像素就被融合掉了 不见了
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        //要绘制的位置和大小，贴纸是画在耳朵上的，直接锁定人脸坐标就可以
        GLES30.glViewport((int)0, (int) 0, mBitmap.getWidth(), mBitmap.getHeight());

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
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId[0]);


        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        GLES30.glDisable(GLES30.GL_BLEND);
    }
}
