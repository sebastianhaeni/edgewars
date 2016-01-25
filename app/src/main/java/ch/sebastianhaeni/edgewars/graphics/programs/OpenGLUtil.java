package ch.sebastianhaeni.edgewars.graphics.programs;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Helper utility class for OpenGL ES.
 */
public class OpenGLUtil {

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * GameRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p/>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;

        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(ESProgram.TAG, glOperation + ": glError " + error);
            Log.e(ESProgram.TAG, "Error details:\n" + getErrorText(error));
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

}
