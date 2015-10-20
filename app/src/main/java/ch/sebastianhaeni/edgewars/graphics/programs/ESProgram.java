package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;

/**
 * Abstract class for OpenGL ES programs containing a vertex and a fragment shader.
 */
public abstract class ESProgram {

    private final Context mContext;
    private final int mProgramHandle;

    /**
     * Constructor
     *
     * @param context app context
     */
    public ESProgram(Context context) {
        mContext = context;
        mProgramHandle = ESShader.loadProgram(getVertexShaderSource(), getFragmentShaderSource());
        bind();
        ESShader.linkProgram(mProgramHandle);
    }

    /**
     * @return gets the app context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * @return gets the handle to this program from OpenGL ES.
     */
    public int getProgramHandle() {
        return mProgramHandle;
    }

    /**
     * @return gets the resource ID of the vertex shader source file
     */
    protected abstract int getVertexShaderSource();

    /**
     * @return gets the resource ID of the fragment shader source file
     */
    protected abstract int getFragmentShaderSource();

    /**
     * Can be overridden by implementations to bind variables.
     */
    public void bind() {
        // no op
    }
}
