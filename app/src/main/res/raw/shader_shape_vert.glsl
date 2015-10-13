// This matrix member variable provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
uniform mat4 u_MVPMatrix;
attribute vec4 a_position;

void main() {
    // The matrix must be included as a modifier of gl_Position.
    // Note that the uMVPMatrix factor *must be first* in order
    // for the matrix multiplication product to be correct.
    gl_Position = u_MVPMatrix * a_position;
}
