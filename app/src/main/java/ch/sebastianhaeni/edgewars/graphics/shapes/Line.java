package ch.sebastianhaeni.edgewars.graphics.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.util.Position;

public class Line extends Shape {

    private static final float WIDTH = 0.2f;

    private FloatBuffer vertexBuffer;
    private int vertexCount;

    public Line(Position source, Position destination) {
        super(source);

        float[] mCoordinates = new float[]{
                // in counterclockwise order:
                0f, (WIDTH * .5f), 0f, // top left
                0f, -(WIDTH * .5f), 0f, // bottom left
                source.getX() - destination.getX(), source.getY() - destination.getY() + (WIDTH * .5f), 0f, // bottom right
                source.getX() - destination.getX(), source.getY() - destination.getY() - (WIDTH * .5f), 0f // top right
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
    public void draw(ShapeProgram shapeProgram, ParticleProgram particleProgram) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(shapeProgram.getProgramHandle());

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(shapeProgram.getPositionHandle());

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                shapeProgram.getPositionHandle(), GameRenderer.COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                GameRenderer.vertexStride, vertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(shapeProgram.getColorHandle(), 1, getColor(), 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);
    }

}
