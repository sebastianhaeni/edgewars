package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.OpenGLUtil;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Draws a line between a source and a destination. The color and width are fixed.
 */
public class Line extends Shape {

    private static final float WIDTH = 0.2f;

    private final FloatBuffer vertexBuffer;
    private final int vertexCount;

    /**
     * Initializes the line with a source and destination rectangle that's really thin. The corners
     * are not rounded.
     *
     * @param src   where the line starts
     * @param dst   where the line ends
     * @param color the color of the edge
     */
    public Line(Position src, Position dst, float[] color) {
        super(src, color);

        float[] mCoordinates = new float[]{
                // in counterclockwise order:
                0f, (WIDTH * .5f), 0f, // top left
                0f, -(WIDTH * .5f), 0f, // bottom left
                src.getX() - dst.getX(), src.getY() - dst.getY() - (WIDTH * .5f), 0f, // bottom right
                src.getX() - dst.getX(), src.getY() - dst.getY() + (WIDTH * .5f), 0f  // top right
        };

        vertexCount = mCoordinates.length / GameRenderer.COORDS_PER_VERTEX;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                mCoordinates.length * 4);

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(mCoordinates);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    @Override
    public void draw(GameRenderer renderer) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(renderer.getShapeProgram().getProgramHandle());
        OpenGLUtil.checkGlError("glUseProgram");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(
                renderer.getShapeProgram().getMVPMatrixHandle(),
                1,
                false,
                renderer.getMVPMatrix(),
                0);
        OpenGLUtil.checkGlError("glUniformMatrix4fv");

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(renderer.getShapeProgram().getPositionHandle());
        OpenGLUtil.checkGlError("glEnableVertexAttribArray");

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                renderer.getShapeProgram().getPositionHandle(), GameRenderer.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                GameRenderer.VERTEX_STRIDE, vertexBuffer);
        OpenGLUtil.checkGlError("glVertexAttribPointer");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(renderer.getShapeProgram().getColorHandle(), 1, getColor(), 0);
        OpenGLUtil.checkGlError("glUniform4fv");

        // draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
        OpenGLUtil.checkGlError("glDrawArrays");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(renderer.getShapeProgram().getPositionHandle());
        OpenGLUtil.checkGlError("glDisableVertexAttribArray");

    }

}
