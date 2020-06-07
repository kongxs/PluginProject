package fu.wanke.opengl;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


//纹理  根据坐标系映射
public class BitmapRenderTexture {


    //顶点坐标
    static float vertexData[] = {   // in counterclockwise order:
            -1f, -1f, 0.0f, // bottom left
            1f, -1f, 0.0f, // bottom right
            -1f, 1f, 0.0f, // top left
            1f, 1f, 0.0f,  // top right
    };

    //纹理坐标  对应顶点坐标  与之映射
    static float textureData[] = {   // in counterclockwise order:
            0f, 1f, 0.0f, // bottom left
            1f, 1f, 0.0f, // bottom right
            0f, 0f, 0.0f, // top left
            1f, 0f, 0.0f,  // top right
    };

    //每一次取点的时候取几个点
    static final int COORDS_PER_VERTEX = 3;

    private final int vertexCount = vertexData.length / COORDS_PER_VERTEX;
    //每一次取的总的点 大小
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private Context context;
    //位置
    private FloatBuffer vertexBuffer;
    //纹理
    private FloatBuffer textureBuffer;
    private int program;
    private int avPosition;
    //纹理位置
    private int afPosition;


    public BitmapRenderTexture(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
    }


    public void onSurfaceCreated() {
        String vertexSource = ShaderUtil.readRawTxt(context, R.raw.vertex_shader);
        String fragmentSource = ShaderUtil.readRawTxt(context, R.raw.fragment_shader);
        program = ShaderUtil.createProgram(vertexSource, fragmentSource);

        if (program > 0) {
            //获取顶点坐标字段
            avPosition = GLES30.glGetAttribLocation(program, "av_Position");
            //获取纹理坐标字段
            afPosition = GLES30.glGetAttribLocation(program, "af_Position");
        }
    }

    public void draw(int textureId) {

        //使用程序
        GLES30.glUseProgram(program);

        //绑定渲染纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);

        GLES30.glEnableVertexAttribArray(avPosition);
        GLES30.glEnableVertexAttribArray(afPosition);
        //设置顶点位置值
        GLES30.glVertexAttribPointer(avPosition, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);
        //设置纹理位置值
        GLES30.glVertexAttribPointer(afPosition, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, textureBuffer);
        //绘制 GLES30.GL_TRIANGLE_STRIP:复用坐标
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, vertexCount);
        GLES30.glDisableVertexAttribArray(avPosition);
        GLES30.glDisableVertexAttribArray(afPosition);

        //解绑纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }

    public void onSurfaceChanged(int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }
}