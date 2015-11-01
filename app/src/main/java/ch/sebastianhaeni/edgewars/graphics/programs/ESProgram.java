package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;

import ch.sebastianhaeni.edgewars.GameApplication;

/**
 * Abstract class for OpenGL ES programs containing a vertex and a fragment shader.
 */
public abstract class ESProgram {

    static final String TAG = "ESProgram";
    private final Context mContext;
    private final int mProgramHandle;

    /**
     * Constructor
     *
     * @param context app context
     */
    ESProgram(Context context) {
        mContext = context;
        mProgramHandle = loadProgram(getVertexShaderSource(), getFragmentShaderSource());
        linkProgram(mProgramHandle);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     * <p/>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       Vertex or fragment shader type.
     * @param shaderCode String containing the shader code.
     * @return Returns an id for the shader.
     */
    private static int loadShader(int type, String shaderCode) {
        int shader;
        int[] compiled = new int[1];

        // Create the shader object
        shader = GLES20.glCreateShader(type);

        if (shader == 0)
            return 0;

        // Load the shader source
        GLES20.glShaderSource(shader, shaderCode);

        // Compile the shader
        GLES20.glCompileShader(shader);

        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    /**
     * Load a vertex and fragment shader, create a program object, link program.
     *
     * @param vertShaderSrc Vertex shader source code
     * @param fragShaderSrc Fragment shader source code
     * @return handle for the program
     */
    private static int loadProgram(int vertShaderSrc, int fragShaderSrc) {
        int vertexShader;
        int fragmentShader;
        int programObject;

        // Load the vertex/fragment shaders
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getCode(vertShaderSrc));
        if (vertexShader == 0) {
            return 0;
        }

        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getCode(fragShaderSrc));
        if (fragmentShader == 0) {
            GLES20.glDeleteShader(vertexShader);
            return 0;
        }

        // Create the program object
        programObject = GLES20.glCreateProgram();

        if (programObject == 0) {
            return 0;
        }

        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);

        return programObject;
    }

    /**
     * Resolves the resource ID with the string from the file.
     *
     * @param resId resource ID
     * @return string of the shader code
     */
    private static String getCode(int resId) {
        try {
            return CharStreams.toString(
                    new InputStreamReader(GameApplication
                            .getAppContext()
                            .getResources()
                            .openRawResource(resId), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Links the program.
     *
     * @param programHandle handle to the program
     */
    private static void linkProgram(int programHandle) {
        int[] linked = new int[1];

        // Link the program
        GLES20.glLinkProgram(programHandle);

        // Check the link status
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            throw new RuntimeException("Error linking program: "
                    + GLES20.glGetProgramInfoLog(programHandle));
        }

        // Free up no longer needed shader resources
        //GLES20.glDeleteShader(vertexShader);
        //GLES20.glDeleteShader(fragmentShader);
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
}
