package ch.sebastianhaeni.edgewars.graphics.programs;

import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

/**
 * This is an OpenGL ES program that is used to render basic shapes with a uni color.
 */
public class ShapeProgram extends ESProgram {

    private final int mPositionHandle;
    private final int mMVPMatrixHandle;
    private final int mColorHandle;

    /**
     * Constructor
     */
    public ShapeProgram() {
        super();

        // get handle to vertex shader' vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(getProgramHandle(), "a_position");

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_MVPMatrix");

        // get handle to shape's color
        mColorHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_color");
    }

    @Override
    protected int getVertexShaderSource() {
        return R.raw.shader_shape_vert;
    }

    @Override
    protected int getFragmentShaderSource() {
        return R.raw.shader_shape_frag;
    }

    /**
     * @return gets the handle to the position
     */
    public int getPositionHandle() {
        return mPositionHandle;
    }

    /**
     * @return gets the handle to the MVP matrix
     */
    public int getMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }

    /**
     * @return gets the handle to the color
     */
    public int getColorHandle() {
        return mColorHandle;
    }
}
