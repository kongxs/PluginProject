package fu.wanke.opengl.render;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fu.wanke.opengl.R;
import fu.wanke.opengl.TexureUtils;
import fu.wanke.opengl.filter.FboFilter;
import fu.wanke.opengl.filter.Filter;
import fu.wanke.opengl.filter.GrayFilter;
import fu.wanke.opengl.filter.WaterFilter;

public class BitmapRender extends BaseRender {

    Filter  filter ;
    GrayFilter grayFilter;
    WaterFilter waterFilter;

    private int textureId;
    private ImageListener mListener;
    private int mwidth;
    private int mheight;
    private ByteBuffer grayImg;

    public BitmapRender(Context context) {
        super(context);

        filter = new Filter(context);
        grayFilter = new GrayFilter(context);
        waterFilter = new WaterFilter(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        super.onSurfaceChanged(gl10, width, height);
        this.mwidth = width;
        this.mheight = height;
        filter.prepare(width,height);
        grayFilter.prepare(width,height);

        textureId = TexureUtils.loadTexture(mContext, R.drawable.mm);

        grayImg = ByteBuffer.allocate(width * height * 4);

        waterFilter.prepare(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {


        textureId = grayFilter.draw(textureId);

        textureId = waterFilter.draw(textureId);

        filter.draw(textureId);

        if(mListener != null){
            grayImg.position(0);
            GLES30.glReadPixels(0, 0, mwidth, mheight, GLES30.GL_RGBA,GLES30.GL_UNSIGNED_BYTE, grayImg);
            mListener.onGetGrayImag(grayImg, mwidth, mheight);
        }
    }

    public void  setListener(ImageListener listener) {
        this.mListener = listener;
    }

    public interface ImageListener {
        void onGetGrayImag(ByteBuffer buffer, int width, int height);
    }
}
