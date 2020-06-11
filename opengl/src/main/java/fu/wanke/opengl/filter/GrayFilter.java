package fu.wanke.opengl.filter;

import android.content.Context;

public class GrayFilter extends FboFilter {

    public GrayFilter(Context mContext) {
        super(mContext);
    }

    @Override
    public void prepare(int width, int height) {
        super.prepare(width, height);
    }

    @Override
    public void resetCooridate() {
        super.resetCooridate();


        fragment_shader = "#version 300 es\n" +
                "precision mediump float;\n" +
                "uniform sampler2D uTextureUnit;\n" +
                "//接收刚才顶点着色器传入的纹理坐标(s,t)\n" +
                "in vec2 vTexCoord;\n" +
                "out vec4 vFragColor;\n" +
                "void main() {\n" +
                "     vec4 color = texture(uTextureUnit,vTexCoord);\n" +
                "     float gray = (color.r + color.g + color.b) / 3.0;\n" +
                "     vec4 finalColor = vec4(gray, gray, gray, color.a);\n" +
                "     vFragColor = finalColor;"+
                "}";


    }


}
