package fu.wanke.opengl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.nio.ByteBuffer;

import fu.wanke.opengl.camera1.Camera1Activity;
import fu.wanke.opengl.color.ColorSurfaceView;
import fu.wanke.opengl.render.BitmapRender;
import fu.wanke.opengl.texture.TextureActivity;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-color");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorSurfaceView surfaceView = findViewById(R.id.gl_surface_view);

        final ImageView imageView = findViewById(R.id.imageviwe);
        imageView.setVisibility(View.GONE);

        surfaceView.setListener(new BitmapRender.ImageListener(){

            @Override
            public void onGetGrayImag(final ByteBuffer buffer, int width, int height) {
                final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        buffer.clear();
                    }
                });
            }
        });
        findViewById(R.id.texture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextureActivity.class));
            }
        });


        findViewById(R.id.surfaceView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Camera1Activity.class));
            }
        });
    }
}
