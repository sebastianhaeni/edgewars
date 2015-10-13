package ch.sebastianhaeni.edgewars.graphics.shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ESShader;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Draws a line between a source and a destination. The color and width are fixed.
 */
public class Line extends Shape {

    private static final float WIDTH = 0.2f;

    private FloatBuffer vertexBuffer;
    private int vertexCount;

    /**
     * Initializes the line with a source and destination rectangle that's really thin. The corners
     * are not rounded.
     *
     * @param source      where the line starts
     * @param destination where the line ends
     */
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
    public void draw(GameRenderer renderer, ShapeProgram shapeProgram, ParticleProgram particleProgram) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(shapeProgram.getProgramHandle());

        // Set the camera position (View matrix)
        Matrix.setLookAtM(
                renderer.getViewMatrix(),
                0,
                renderer.getGameState().getCamera().getX() + getPosition().getX(),
                renderer.getGameState().getCamera().getY() + getPosition().getY(),
                -GameRenderer.EYE_HEIGHT,
                renderer.getGameState().getCamera().getX() + getPosition().getX(),
                renderer.getGameState().getCamera().getY() + getPosition().getY(),
                0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(renderer.getMVPMatrix(), 0, renderer.getProjectionMatrix(), 0, renderer.getViewMatrix(), 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(shapeProgram.getMVPMatrixHandle(), 1, false, renderer.getMVPMatrix(), 0);
        ESShader.checkGlError("glUniformMatrix4fv");

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

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(shapeProgram.getPositionHandle());
    }

}
