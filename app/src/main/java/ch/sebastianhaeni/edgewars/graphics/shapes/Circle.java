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

public class Circle extends Shape {
    private static final int CORNERS = 364;
    private static boolean verticesInitialized = false;
    private static float vertices[] = new float[CORNERS * 3];

    private final FloatBuffer mVertexBuffer;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     *
     * @param position Position of the circle
     */
    public Circle(Position position) {
        super(position);

        if (!verticesInitialized) {
            vertices[0] = 0;
            vertices[1] = 0;
            vertices[2] = 0;
            for (int i = 1; i < CORNERS; i++) {
                vertices[(i * 3)] = (float) (0.5 * Math.cos((3.14 / 180) * (float) i));
                vertices[(i * 3) + 1] = (float) (0.5 * Math.sin((3.14 / 180) * (float) i));
                vertices[(i * 3) + 2] = 0;
            }
            verticesInitialized = true;
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
                renderer.getGameState().getCamera().getX() +getPosition().getX(),
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
                GameRenderer.vertexStride, mVertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(shapeProgram.getColorHandle(), 1, getColor(), 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, CORNERS);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(shapeProgram.getPositionHandle());
    }

}
