package fu.wanke.opengl.filter;

import android.content.Context;
import android.opengl.GLES30;

import java.nio.FloatBuffer;

import fu.wanke.opengl.ShaderUtils;
import fu.wanke.opengl.render.BaseRender;

public class Shape_FBO {


    private int mWindowProgram;
    private FloatBuffer mSqureBufferfbo;


    public Shape_FBO(Context context) {

        this.initVetexData();

    }

    public void initVetexData() {

        float [] fboVertex = new float[] {

                -1f,-1f,  0,1,

                -1f,1f,  0,0,

                1f,-1f,  1,1,

                1f,1f,  1,0
        };

        mSqureBufferfbo = BaseRender.toBuffer(fboVertex);
    }


    public void draw(int texturesId) {

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);


        mWindowProgram = ShaderUtils.loaderShader(windowVertexShaderCode,
                windowFragmentShaderCode);

        int positionHandle = GLES30.glGetAttribLocation(mWindowProgram, "aPosition");

        int textureCoordHandle = GLES30.glGetAttribLocation(mWindowProgram, "aTextureCoord");

        int textureHandle = GLES30.glGetUniformLocation(mWindowProgram, "uTexture");


        GLES30.glVertexAttribPointer(positionHandle, 2, GLES30.GL_FLOAT, false, (2+2) * 4, mSqureBufferfbo);


        GLES30.glVertexAttribPointer(textureCoordHandle, 2, GLES30.GL_FLOAT, false, (2+2) * 4, mSqureBufferfbo);

        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glEnableVertexAttribArray(textureCoordHandle);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texturesId);

        GLES30.glUniform1i(textureHandle, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
    }

    private String windowVertexShaderCode = ""

            + "attribute vec2 aPosition;"

            + "attribute vec2 aTextureCoord;"

            + "varying vec2 vTextureCoord;"

            + "void main(){"

            + "gl_Position = vec4(aPosition,0,1);"

            + "vTextureCoord = aTextureCoord;"

            + "}";

    private String windowFragmentShaderCode =

            "precision mediump float;"

            + "uniform sampler2D uTexture;"

            + "varying vec2 vTextureCoord;"

            + "void main(){"

            + "gl_FragColor = texture2D(uTexture, vTextureCoord);"

            + "}";

}
