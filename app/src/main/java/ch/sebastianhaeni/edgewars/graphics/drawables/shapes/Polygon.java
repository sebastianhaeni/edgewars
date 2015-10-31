package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.OpenGLUtil;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Draws a circle with a uniform color at a certain position.
 */
public class Polygon extends Shape {

    private final FloatBuffer mVertexBuffer;
    private final int mCorners;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     *
     * @param position Position of the circle
     * @param color    Color of this polygon
     * @param corners  the amount of corners this polygon should have
     * @param angle    angle of the polygon
     */
    public Polygon(Position position, float[] color, int corners, int angle) {
        super(position, color);

        mCorners = corners;
        float[] vertices = new float[corners * 3];

        float step = 360f / corners;
        for (int i = 0; i < corners; i++) {
            vertices[(i * 3)] = (float) (0.5 * Math.cos((3.14 / 180) * ((step * i) + angle)));
            vertices[(i * 3) + 1] = (float) (0.5 * Math.sin((3.14 / 180) * ((step * i) + angle)));
            vertices[(i * 3) + 2] = 0;
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        mVertexBuffer.position(0);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     */
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
                renderer.getShapeProgram().getPositionHandle(),
                GameRenderer.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                GameRenderer.VERTEX_STRIDE,
                mVertexBuffer);
        OpenGLUtil.checkGlError("glVertexAttribPointer");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(renderer.getShapeProgram().getColorHandle(), 1, getColor(), 0);
        OpenGLUtil.checkGlError("glUniform4fv");

        // draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mCorners);
        OpenGLUtil.checkGlError("glDrawArrays");

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(renderer.getShapeProgram().getPositionHandle());
        OpenGLUtil.checkGlError("glDisableVertexAttribArray");

    }

}
