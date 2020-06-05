package fu.wanke.opengl.camera1;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera1Helper camera1Helper;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        camera1Helper = new Camera1Helper(getContext());
        holder = getHolder();
        holder.addCallback(this);

    }

    public void startVideo() {
        camera1Helper.startRecorder();
    }

    public void stopRecorder() {
        camera1Helper.stopRecorder();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera1Helper.open(holder,true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
