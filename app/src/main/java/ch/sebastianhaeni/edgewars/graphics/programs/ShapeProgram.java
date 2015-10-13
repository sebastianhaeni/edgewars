package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

public class ShapeProgram extends ESProgram {

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
    protected int getVertexShaderSource() {
        return R.raw.shader_shape_vert;
    }

    @Override
    protected int getFragmentShaderSource() {
        return R.raw.shader_shape_frag;
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
