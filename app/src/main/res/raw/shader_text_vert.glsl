uniform mat4 u_MVPMatrix[24];
attribute float a_MVPMatrixIndex;
attribute vec4 a_position;
attribute vec2 a_texCoordinate;
varying vec2 v_texCoordinate;

void main() {
   int mvpMatrixIndex = int(a_MVPMatrixIndex);
   v_texCoordinate = a_texCoordinate;
   gl_Position = u_MVPMatrix[mvpMatrixIndex] * a_position;
}
