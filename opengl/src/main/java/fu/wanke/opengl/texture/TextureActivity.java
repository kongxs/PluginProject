package fu.wanke.opengl.texture;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import fu.wanke.opengl.R;
import fu.wanke.opengl.camera1.Camera1Helper;

public class TextureActivity extends Activity implements TextureView.SurfaceTextureListener {

    private TextureView texture_view;
    private Camera1HelperTexture helper;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture);

        texture_view = findViewById(R.id.texture_view);
        helper = new Camera1HelperTexture(this);

        texture_view.setSurfaceTextureListener(this);
        texture_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.autoFocus();
            }
        });

        findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.startRecorder();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.stopRecorder();
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        helper.open(surface, true);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
