package fu.wanke.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class ShaderUtil {

    static String TAG = "ShaderUtil";

    public static String readRawTxt(Context context, int rawId) {
        InputStream inputStream = context.getResources().openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int loadShader(int shaderType, String source) {
        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            //添加代码到shader
            GLES30.glShaderSource(shader, source);
            //编译shader
            GLES30.glCompileShader(shader);
            int[] compile = new int[1];
            //检测是否编译成功
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compile, 0);
            if (compile[0] != GLES30.GL_TRUE) {
                Log.d(TAG, "shader compile error");
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        //获取vertex shader
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        //获取fragment shader
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        //创建一个空的渲染程序
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            //添加vertexShader到渲染程序
            GLES30.glAttachShader(program, vertexShader);
            //添加fragmentShader到渲染程序
            GLES30.glAttachShader(program, fragmentShader);
            //关联为可执行渲染程序
            GLES30.glLinkProgram(program);
            int[] linsStatus = new int[1];
            //检测是否关联成功
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linsStatus, 0);
            if (linsStatus[0] != GLES30.GL_TRUE) {
                Log.d(TAG, "link program error");
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }


    public static Bitmap createTextImage(String text, int textSize, String textColor, String bgColor, int padding) {

        Paint paint = new Paint();
        paint.setColor(Color.parseColor(textColor));
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        float width = paint.measureText(text, 0, text.length());

        float top = paint.getFontMetrics().top;
        float bottom = paint.getFontMetrics().bottom;

        Bitmap bm = Bitmap.createBitmap((int) (width + padding * 2), (int) ((bottom - top) + padding * 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);

        canvas.drawColor(Color.parseColor(bgColor));
        canvas.drawText(text, padding, -top + padding, paint);
        return bm;
    }

    public static int loadBitmapTexture(Bitmap bitmap) {
        int[] textureIds = new int[1];
        GLES30.glGenTextures(1, textureIds, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        ByteBuffer bitmapBuffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getWidth() * 4);//ARGB
        bitmap.copyPixelsToBuffer(bitmapBuffer);
        bitmapBuffer.flip();

        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, bitmap.getWidth(),
                bitmap.getHeight(), 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, bitmapBuffer);
        return textureIds[0];
    }
}
