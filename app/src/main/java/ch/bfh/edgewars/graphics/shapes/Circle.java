package ch.bfh.edgewars.graphics.shapes;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ch.bfh.edgewars.graphics.GameRenderer;
import ch.bfh.edgewars.util.Position;

public class Circle extends Shape {
    private static final int corners = 364;
    private final FloatBuffer vertexBuffer;
    private int mColorHandle;

    private static float vertices[] = new float[corners * 3];

    float color[] = {0.63671875f, 0.16953125f, 0.22265625f, 0.0f};

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     * @param position Position of the circle
     */
    public Circle(Position position) {
        super(position);

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        for (int i = 1; i < corners; i++) {
            vertices[(i * 3)] = (float) (0.5 * Math.cos((3.14 / 180) * (float) i));
            vertices[(i * 3) + 1] = (float) (0.5 * Math.sin((3.14 / 180) * (float) i));
            vertices[(i * 3) + 2] = 0;
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertices);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     */
    @Override
    public void draw(int program, int positionHandle) {
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                positionHandle, GameRenderer.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                GameRenderer.vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, corners);
    }
}
