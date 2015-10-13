package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

/**
 * Open GL ES program to render particles.
 */
public class ParticleProgram extends ESProgram {

    // Attribute locations
    private int mLifetimeLoc;
    private int mStartPositionLoc;
    private int mEndPositionLoc;

    // Uniform location
    private int mTimeLoc;
    private int mColorLoc;
    private int mCenterPositionLoc;
    private int mSamplerLoc;

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
        mCenterPositionLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_centerPosition");
        mColorLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_color");
        mSamplerLoc = GLES20.glGetUniformLocation(getProgramHandle(), "s_texture");
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
    public int getLifetimeLoc() {
        return mLifetimeLoc;
    }

    /**
     * @return gets the handle to the position
     */
    public int getEndPositionLoc() {
        return mEndPositionLoc;
    }

    /**
     * @return gets the handle to the start position
     */
    public int getStartPositionLoc() {
        return mStartPositionLoc;
    }

    /**
     * @return gets the handle to the sampler
     */
    public int getSamplerLoc() {
        return mSamplerLoc;
    }

    /**
     * @return gets the handle to the center position
     */
    public int getCenterPositionLoc() {
        return mCenterPositionLoc;
    }

    /**
     * @return gets the handle to the color
     */
    public int getColorLoc() {
        return mColorLoc;
    }

    /**
     * @return gets the handle to the time
     */
    public int getTimeLoc() {
        return mTimeLoc;
    }
}
