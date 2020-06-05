package fu.wanke.opengl;

import android.opengl.GLES30;

public class ShaderUtils {

//    /**
//     * 编译顶点着色器
//     *
//     * @param shaderCode
//     * @return
//     */
//    public static int compileVertexShader(String shaderCode) {
//        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode);
//    }
//
//    /**
//     * 编译片段着色器
//     *
//     * @param shaderCode
//     * @return
//     */
//    public static int compileFragmentShader(String shaderCode) {
//        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode);
//    }
//
//    public static int compileShader(int type, String shaderCode) {
//        //创建一个着色器
//        final int shaderId = GLES30.glCreateShader(type);
//        if (shaderId != 0) {
//            //加载到着色器
//            GLES30.glShaderSource(shaderId, shaderCode);
//            //编译着色器
//            GLES30.glCompileShader(shaderId);
//            //检测状态
//            final int[] compileStatus = new int[1];
//            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
//            if (compileStatus[0] == 0) {
//                String logInfo = GLES30.glGetShaderInfoLog(shaderId);
//                System.err.println(logInfo);
//                //创建失败
//                GLES30.glDeleteShader(shaderId);
//                return 0;
//            }
//            return shaderId;
//        } else {
//            //创建失败
//            return 0;
//        }
//    }
//
//    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
//        final int programId = GLES30.glCreateProgram();
//        if (programId != 0) {
//            //将顶点着色器加入到程序
//            GLES30.glAttachShader(programId, vertexShaderId);
//            //将片元着色器加入到程序中
//            GLES30.glAttachShader(programId, fragmentShaderId);
//            //链接着色器程序
//            GLES30.glLinkProgram(programId);
//            final int[] linkStatus = new int[1];
//            GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0);
//            if (linkStatus[0] == 0) {
//                String logInfo = GLES30.glGetProgramInfoLog(programId);
//                System.err.println(logInfo);
//                GLES30.glDeleteProgram(programId);
//                return 0;
//            }
//            return programId;
//        } else {
//            //创建失败
//            return 0;
//        }
//    }

    public static int loaderShader(String vertex , String fragment) {

        int vertexId = compileVertex(vertex);
        int fragmentId = compileFragment(fragment);

        int programId = linkProgram(vertexId,fragmentId);

        if (programId != 0) {
            GLES30.glUseProgram(programId);
        }

        return programId;
    }

    private static int linkProgram(int vertexId, int fragmentId) {

        final int programId = GLES30.glCreateProgram();

        if (programId != 0) {

            //将顶点着色器加入到程序
            GLES30.glAttachShader(programId, vertexId);
            //将片元着色器加入到程序中
            GLES30.glAttachShader(programId, fragmentId);
            //链接着色器程序
            GLES30.glLinkProgram(programId);

            return programId;
        }
        return 0;
    }

    /**
     * @param type  顶点着色器:GLES30.GL_VERTEX_SHADER
     *      *               片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param type
     * @param shaderCode
     * @return
     */
    private static int compile(int type, String shaderCode) {

        //创建一个着色器
        final int shaderId = GLES30.glCreateShader(type);
        if (shaderId != 0 ) {
            GLES30.glShaderSource(shaderId ,  shaderCode);
            GLES30.glCompileShader(shaderId);

            //检测状态
            return shaderId;
        }

        return 0;
    }

    private static int compileVertex(String vertex) {
        return compile(GLES30.GL_VERTEX_SHADER , vertex);
    }

    private static int compileFragment(String vertex) {
        return compile(GLES30.GL_FRAGMENT_SHADER , vertex);
    }


}
