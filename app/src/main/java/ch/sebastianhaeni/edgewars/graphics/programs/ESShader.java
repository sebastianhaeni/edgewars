package ch.sebastianhaeni.edgewars.graphics.programs;

import android.opengl.GLES20;
import android.util.Log;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;

import ch.sebastianhaeni.edgewars.GameApplication;

/**
 * Helper utility class for loading and compiling OpenGL ES shaders and programs.
 */
public class ESShader {

    private static final String TAG = "ESShader";

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
    public static int loadShader(int type, String shaderCode) {
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
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * GameRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;

        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            Log.e(TAG, "Error details:\n" + getErrorText(error));
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Gets a text representation and explanation of the GL error code.
     *
     * @param glErrorCode error code from <code>GLES20.glGetError()</code>
     * @return name and explanation of the error
     */
    private static String getErrorText(int glErrorCode) {
        switch (glErrorCode) {
            case GLES20.GL_INVALID_ENUM:
                return "GL_INVALID_ENUM\nGiven when an enumeration parameter is not a legal enume" +
                        "ration for that function. This is given only for local problems; if the " +
                        "spec allows the enumeration in certain circumstances, where other parame" +
                        "ters or state dictate those circumstances, then GL_INVALID_OPERATION is " +
                        "the result instead.";
            case GLES20.GL_INVALID_VALUE:
                return "GL_INVALID_VALUE\nGiven when a value parameter is not a legal value for t" +
                        "hat function. This is only given for local problems; if the spec allows " +
                        "the value in certain circumstances, where other parameters or state dict" +
                        "ate those circumstances, then GL_INVALID_OPERATION is the result instead.";
            case GLES20.GL_INVALID_OPERATION:
                return "GL_INVALID_OPERATION\nGiven when the set of state for a command is not le" +
                        "gal for the parameters given to that command. It is also given for comma" +
                        "nds where combinations of parameters define what the legal parameters ar" +
                        "e.";
            case GLES20.GL_OUT_OF_MEMORY:
                return "GL_OUT_OF_MEMORY\nGiven when performing an operation that can allocate me" +
                        "mory, and the memory cannot be allocated. The results of OpenGL function" +
                        "s that return this error are undefined; it is allowable for partial oper" +
                        "ations to happen.";
            case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
                return "GL_INVALID_FRAMEBUFFER_OPERATION\nGiven when doing anything that would at" +
                        "tempt to read from or write/render to a framebuffer that is not complete.";
            default:
                return "Unknown error";
        }
    }

    /**
     * Load a vertex and fragment shader, create a program object, link program.
     *
     * @param vertShaderSrc Vertex shader source code
     * @param fragShaderSrc Fragment shader source code
     * @return handle for the program
     */
    public static int loadProgram(int vertShaderSrc, int fragShaderSrc) {
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

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

        // Link the program
        GLES20.glLinkProgram(programObject);

        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            throw new RuntimeException("Error linking program: "
                    + GLES20.glGetProgramInfoLog(programObject));
        }

        // Free up no longer needed shader resources
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

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

}
