package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;

public abstract class ESProgram {

    private final Context mContext;
    private final int mProgramHandle;

    public ESProgram(Context context) {
        mContext = context;

        mProgramHandle = ESShader.loadProgram(getVertexShaderSource(), getFragmentShaderSource());
    }

    public Context getContext() {
        return mContext;
    }

    public int getProgramHandle() {
        return mProgramHandle;
    }

    protected abstract String getVertexShaderSource();

    protected abstract String getFragmentShaderSource();
}
