package fu.wanke.opengl.camera1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import fu.wanke.opengl.R;

public class Camera1Activity extends Activity {


    private CameraSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);


        surfaceView = findViewById(R.id.surfaceView);


        findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.startVideo();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.stopRecorder();
            }
        });
    }

}
