package ch.sebastianhaeni.edgewars.graphics.programs;

import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

/**
 * Open GL ES program to render particles.
 */
public class ParticleProgram extends ESProgram {

    // Attribute locations
    private final int mMVPMatrixHandle;
    private final int mLifetimeHandle;
    private final int mStartPositionHandle;
    private final int mEndPositionHandle;

    // Uniform location
    private final int mTimeHandle;
    private final int mColorHandle;

    /**
     * Constructor
     *
     */
    public ParticleProgram() {
        super();

        // Get the attribute locations
        mLifetimeHandle = GLES20.glGetAttribLocation(getProgramHandle(), "a_lifetime");
        mStartPositionHandle = GLES20.glGetAttribLocation(getProgramHandle(), "a_startPosition");
        mEndPositionHandle = GLES20.glGetAttribLocation(getProgramHandle(), "a_endPosition");

        // Get the uniform locations
        mTimeHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_time");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_MVPMatrix");
        mColorHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_color");
    }

    @Override
    protected int getVertexShaderSource() {
        return R.raw.shader_particle_vert;
    }

    @Override
    protected int getFragmentShaderSource() {
        return R.raw.shader_particle_frag;
    }

    /**
     * @return gets the handle to the particle life time
     */
    public int getLifetimeHandle() {
        return mLifetimeHandle;
    }

    /**
     * @return gets the handle to the position
     */
    public int getEndPositionHandle() {
        return mEndPositionHandle;
    }

    /**
     * @return gets the handle to the start position
     */
    public int getStartPositionHandle() {
        return mStartPositionHandle;
    }

    /**
     * @return gets the handle to the color
     */
    public int getColorHandle() {
        return mColorHandle;
    }

    /**
     * @return gets the handle to the time
     */
    public int getTimeHandle() {
        return mTimeHandle;
    }

    /**
     * @return gets the handel to the mvp matrix
     */
    public int getMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }
}
