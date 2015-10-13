package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

public class ShapeProgram extends ESProgram {

    private static final String VERTEX_SHADER_SRC =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private static final String FRAGMENT_SHADER_SRC =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int mPositionHandle;
    private int mMVPMatrixHandle;
    private int mColorHandle;

    public ShapeProgram(Context context) {
        super(context);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(getProgramHandle(), "vPosition");

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramHandle(), "uMVPMatrix");

        // get handle to shape's color
        mColorHandle = GLES20.glGetUniformLocation(getProgramHandle(), "vColor");
    }

    @Override
    protected String getVertexShaderSource() {
        return VERTEX_SHADER_SRC;
    }

    @Override
    protected String getFragmentShaderSource() {
        return FRAGMENT_SHADER_SRC;
    }

    public int getPositionHandle() {
        return mPositionHandle;
    }

    public int getMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }

    public int getColorHandle() {
        return mColorHandle;
    }
}
