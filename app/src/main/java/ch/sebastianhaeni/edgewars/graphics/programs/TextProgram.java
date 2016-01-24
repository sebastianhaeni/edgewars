package ch.sebastianhaeni.edgewars.graphics.programs;

import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

public class TextProgram extends ESProgram {

    private final int mPositionHandle;
    private final int mTexCoordinateLoc;
    private final int mColorHandle;
    private final int mMatrixHandle;
    private final int mSamplerLoc;

    /**
     * Constructor
     */
    public TextProgram() {
        super();

        mPositionHandle = GLES20.glGetAttribLocation(getProgramHandle(), "vPosition");
        mTexCoordinateLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_texCoord");
        mColorHandle = GLES20.glGetAttribLocation(getProgramHandle(), "a_Color");
        mMatrixHandle = GLES20.glGetUniformLocation(getProgramHandle(), "uMVPMatrix");
        mSamplerLoc = GLES20.glGetUniformLocation(getProgramHandle(), "s_texture");
    }

    @Override
    protected int getVertexShaderSource() {
        return R.raw.shader_text_vert;
    }

    @Override
    protected int getFragmentShaderSource() {
        return R.raw.shader_text_frag;
    }

    public int getPositionHandle() {
        return mPositionHandle;
    }

    public int getTexCoordinateLoc() {
        return mTexCoordinateLoc;
    }

    public int getColorHandle() {
        return mColorHandle;
    }

    public int getMatrixHandle() {
        return mMatrixHandle;
    }

    public int getSamplerLoc() {
        return mSamplerLoc;
    }
}
