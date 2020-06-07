package fu.wanke.opengl.color;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import fu.wanke.opengl.render.BitmapRender;
import fu.wanke.opengl.render.CameraSurfaceRenderer;
import fu.wanke.opengl.render.FBORender;
import fu.wanke.opengl.render.RectangleRenderer;
import fu.wanke.opengl.render.TestRender;
import fu.wanke.opengl.render.TexureRender;
import fu.wanke.opengl.render.TriangleRender;

public class ColorSurfaceView extends GLSurfaceView {
    public ColorSurfaceView(Context context) {
        super(context);
        init();
    }

    public ColorSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
//        setRenderer(new ColorRender(getContext()));
//        setRenderer(new NativeColorRenderer(getContext()));
//        setRenderer(new SimpleRenderer(getContext()));
//        setRenderer(new SimpleColorRenderer(getContext()));
//        setRenderer(new TriangleRender(getContext()));
//        setRenderer(new RectangleRenderer(getContext()));
        setRenderer(new BitmapRender(getContext()));
//        setRenderer(new TestRender(getContext()));
//        setRenderer(new CameraSurfaceRenderer(getContext(),this));
//        setRenderer(new FBORender(getContext()));
//        setRenderer(new TexureRender(getContext()));
        setRenderMode(RENDERMODE_CONTINUOUSLY);

    }


}
