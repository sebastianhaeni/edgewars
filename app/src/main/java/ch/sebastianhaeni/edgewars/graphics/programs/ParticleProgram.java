package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

/**
 * Open GL ES program to render particles.
 */
public class ParticleProgram extends ESProgram {

    // Attribute locations
    private final int mMVPMatrixHandle;
    private int mLifetimeLoc;
    private int mStartPositionLoc;
    private int mEndPositionLoc;

    // Uniform location
    private int mTimeLoc;
    private int mColorHandle;

    /**
     * Constructor
     *
     * @param context app context
     */
    public ParticleProgram(Context context) {
        super(context);

        // Get the attribute locations
        mLifetimeLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_lifetime");
        mStartPositionLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_startPosition");
        mEndPositionLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_endPosition");

        // Get the uniform locations
        mTimeLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_time");
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
        return mLifetimeLoc;
    }

    /**
     * @return gets the handle to the position
     */
    public int getEndPositionHandle() {
        return mEndPositionLoc;
    }

    /**
     * @return gets the handle to the start position
     */
    public int getStartPositionHandle() {
        return mStartPositionLoc;
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
        return mTimeLoc;
    }

    public int getMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }
}
