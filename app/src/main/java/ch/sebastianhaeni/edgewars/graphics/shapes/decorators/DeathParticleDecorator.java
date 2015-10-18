package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import android.opengl.GLES20;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ESShader;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

/**
 * Decorates a shape with death particles. Duration of animation is 1s.
 */
public class DeathParticleDecorator extends DrawableDecorator {
    private final int PARTICLE_SIZE = 7;
    private final int NUM_PARTICLES = 1000;

    // Update time
    private float mTime;
    private long mLastTime;

    private FloatBuffer mParticles;

    /**
     * Initializes particle data. Texture is not loaded because that has
     * to be loaded in the graphics thread. This constructor is called in the game update thread.
     *
     * @param shape The shape this decorator decorates
     */
    public DeathParticleDecorator(Shape shape) {
        super(shape);

        // Fill in particle data array
        Random generator = new Random();

        float[] particleData = new float[NUM_PARTICLES * PARTICLE_SIZE];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            // Life time of particle
            particleData[i * 7] = generator.nextFloat();

            // End position of particle
            double angle = (Math.random() * Math.PI * 2);
            particleData[i * 7 + 1] = (float) ((generator.nextFloat() * 8.0f - 4.0f) * Math.cos(angle));
            particleData[i * 7 + 2] = (float) ((generator.nextFloat() * 8.0f - 4.0f) * Math.sin(angle));
            particleData[i * 7 + 3] = generator.nextFloat() * 8.0f - 4.0f;

            // Start position of particle
            particleData[i * 7 + 4] = generator.nextFloat() * 0.25f - 0.125f;
            particleData[i * 7 + 5] = generator.nextFloat() * 0.25f - 0.125f;
            particleData[i * 7 + 6] = generator.nextFloat() * 0.25f - 0.125f;
        }
        mParticles = ByteBuffer.allocateDirect(particleData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mParticles.put(particleData).position(0);

        // Initialize time to cause reset on first update
        mTime = 1.0f;
    }

    @Override
    public void draw(GameRenderer renderer) {
        update();

        // Use the program object
        GLES20.glUseProgram(renderer.getParticleProgram().getProgramHandle());
        ESShader.checkGlError("glUseProgram");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(
                renderer.getParticleProgram().getMVPMatrixHandle(),
                1,
                false,
                renderer.getMVPMatrix(),
                0);
        ESShader.checkGlError("glUniformMatrix4fv");

        // Load uniform time variable
        GLES20.glUniform1f(renderer.getParticleProgram().getTimeHandle(), mTime);
        ESShader.checkGlError("glUniform1f");

        // set color of base shape
        GLES20.glUniform4f(
                renderer.getParticleProgram().getColorHandle(),
                getShape().getColor()[0],
                getShape().getColor()[1],
                getShape().getColor()[2],
                .5f);
        ESShader.checkGlError("glUniform4f");

        // Load the vertex attributes
        mParticles.position(0);
        GLES20.glVertexAttribPointer(
                renderer.getParticleProgram().getLifetimeHandle(),
                1,
                GLES20.GL_FLOAT,
                false,
                PARTICLE_SIZE * 4,
                mParticles);
        ESShader.checkGlError("glVertexAttribPointer");

        mParticles.position(1);
        GLES20.glVertexAttribPointer(
                renderer.getParticleProgram().getEndPositionHandle(),
                3,
                GLES20.GL_FLOAT,
                false,
                PARTICLE_SIZE * 4,
                mParticles);
        ESShader.checkGlError("glVertexAttribPointer");

        mParticles.position(4);
        GLES20.glVertexAttribPointer(
                renderer.getParticleProgram().getStartPositionHandle(),
                3,
                GLES20.GL_FLOAT,
                false,
                PARTICLE_SIZE * 4,
                mParticles);
        ESShader.checkGlError("glVertexAttribPointer");

        GLES20.glEnableVertexAttribArray(renderer.getParticleProgram().getLifetimeHandle());
        GLES20.glEnableVertexAttribArray(renderer.getParticleProgram().getEndPositionHandle());
        GLES20.glEnableVertexAttribArray(renderer.getParticleProgram().getStartPositionHandle());

        // Blend particles
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

        // Drawing the actual points
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_PARTICLES);

        // Disable blend particles
        GLES20.glDisable(GLES20.GL_BLEND);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(renderer.getParticleProgram().getLifetimeHandle());
        GLES20.glDisableVertexAttribArray(renderer.getParticleProgram().getEndPositionHandle());
        GLES20.glDisableVertexAttribArray(renderer.getParticleProgram().getStartPositionHandle());
    }

    /**
     * Updates the time and position attributes of the particle system.
     */
    private void update() {
        if (mLastTime == 0) {
            mLastTime = SystemClock.uptimeMillis();
        }
        long curTime = SystemClock.uptimeMillis();
        long elapsedTime = curTime - mLastTime;
        float deltaTime = elapsedTime / 1000.0f;
        mLastTime = curTime;

        mTime += deltaTime;

        if (mTime >= 1.0f) {
            mTime = 0.0f;
        }
    }

}
