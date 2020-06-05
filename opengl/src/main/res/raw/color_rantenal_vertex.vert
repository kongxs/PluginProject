
#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec4 aColor;
out vec4 vColor;
void main() {
     gl_Position  = vPosition;
     gl_PointSize = 10.0;
     vColor = aColor;
}

#version 300 es
precision mediump float;
in vec4 vColor;
out vec4 fragColor;
void main() {
     fragColor = vColor;
}
 ---------------------

SimpleRenderer

 #version 300 es
 layout (location = 0) in vec4 vPosition;
 void main() {
      gl_Position  = vPosition;
      gl_PointSize = 10.0;
 }

 #version 300 es
 precision mediump float;
 out vec4 fragColor;
 void main() {
      fragColor = vec4(1.0,1.0,1.0,1.0);
 }


#version 300 es
layout (location=0) in vec4 vPos;
void main(){
    gl_Position = vPos;
    gl_PointSize=10.0;
}

#version 300 es
precision mediump float;
out vec4 fragColor;
uniform vec4 color;
void main(){
    fragColor= color;
}



