package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

import ch.sebastianhaeni.edgewars.R;

public class BatchTextProgram extends ESProgram {

    private int mPositionHandle = 1;
    private int mTexCoordinateHandle = 2;
    private int mMVPMatrixIndexHandle = 3;
    private final int mColorHandle;
    private final int mTextureUniformHandle;
    private final int mMVPMatrixHandle;

    /**
     * Constructor
     *
     * @param context app context
     */
    public BatchTextProgram(Context context) {
        super(context);

        mColorHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_color");
        mTextureUniformHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_texture");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramHandle(), "u_MVPMatrix");
    }

    @Override
    public void bind() {
        //GLES20.glBindAttribLocation(getProgramHandle(), mPositionHandle, "a_position");
        ESShader.checkGlError("glBindAttribLocation");

        //GLES20.glBindAttribLocation(getProgramHandle(), mTexCoordinateHandle, "a_texCoordinate");
        ESShader.checkGlError("glBindAttribLocation");

        //GLES20.glBindAttribLocation(getProgramHandle(), mMVPMatrixIndexHandle, "a_MVPMatrixIndex");
        ESShader.checkGlError("glBindAttribLocation");
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

    public int getMVPMatrixIndexHandle() {
        return mMVPMatrixIndexHandle;
    }

    public int getTexCoordinateHandle() {
        return mTexCoordinateHandle;
    }

    public int getColorHandle() {
        return mColorHandle;
    }

    public int getTextureUniformHandle() {
        return mTextureUniformHandle;
    }

    public int getMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }
}
