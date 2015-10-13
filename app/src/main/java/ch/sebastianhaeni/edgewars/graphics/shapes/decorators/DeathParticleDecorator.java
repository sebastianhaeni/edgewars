package ch.sebastianhaeni.edgewars.graphics.shapes.decorators;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.SystemClock;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import ch.sebastianhaeni.edgewars.GameApplication;
import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

/**
 * Decorates a shape with death particles. Duration of animation is 1s.
 */
public class DeathParticleDecorator extends DrawableDecorator {
    private final int PARTICLE_SIZE = 7;
    private final int NUM_PARTICLES = 1000;

    // Texture handle
    private int mTextureId;

    // Update time
    private float mTime;
    private long mLastTime;

    private FloatBuffer mParticles;
    private boolean mTextureLoaded;

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

        float[] mParticleData = new float[NUM_PARTICLES * PARTICLE_SIZE];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            // Lifetime of particle
            mParticleData[i * 7] = generator.nextFloat();

            // End position of particle
            mParticleData[i * 7 + 1] = generator.nextFloat() * 2.0f - 1.0f;
            mParticleData[i * 7 + 2] = generator.nextFloat() * 2.0f - 1.0f;
            mParticleData[i * 7 + 3] = generator.nextFloat() * 2.0f - 1.0f;

            // Start position of particle
            mParticleData[i * 7 + 4] = generator.nextFloat() * 0.25f - 0.125f;
            mParticleData[i * 7 + 5] = generator.nextFloat() * 0.25f - 0.125f;
            mParticleData[i * 7 + 6] = generator.nextFloat() * 0.25f - 0.125f;
        }
        mParticles = ByteBuffer.allocateDirect(mParticleData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mParticles.put(mParticleData).position(0);

        // Initialize time to cause reset on first update
        mTime = 1.0f;
    }

    /**
     * Load texture from resource in graphics thread.
     *
     * @param is stream of texture
     */
    private void loadTexture(InputStream is) {
        int[] textureId = new int[1];
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeStream(is);
        byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 3];

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = bitmap.getPixel(x, y);
                buffer[(y * bitmap.getWidth() + x) * 3] = (byte) ((pixel >> 16) & 0xFF);
                buffer[(y * bitmap.getWidth() + x) * 3 + 1] = (byte) ((pixel >> 8) & 0xFF);
                buffer[(y * bitmap.getWidth() + x) * 3 + 2] = (byte) ((pixel) & 0xFF);
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 3);
        byteBuffer.put(buffer).position(0);

        GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, bitmap.getWidth(), bitmap.getHeight(), 0,
                GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, byteBuffer);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        mTextureId = textureId[0];
    }

    @Override
    public void draw(GameRenderer renderer, ShapeProgram shapeProgram, ParticleProgram particleProgram) {
        if (!mTextureLoaded) {
            loadTexture(GameApplication.getAppContext().getResources().openRawResource(R.raw.smoke));
            mTextureLoaded = true;
        }

        // Use the program object
        GLES20.glUseProgram(particleProgram.getProgramHandle());

        // update particles
        update(particleProgram);

        // Load the vertex attributes
        mParticles.position(0);
        GLES20.glVertexAttribPointer(particleProgram.getLifetimeLoc(), 1, GLES20.GL_FLOAT,
                false, PARTICLE_SIZE * 4,
                mParticles);

        mParticles.position(1);
        GLES20.glVertexAttribPointer(particleProgram.getEndPositionLoc(), 3, GLES20.GL_FLOAT,
                false, PARTICLE_SIZE * 4,
                mParticles);

        mParticles.position(4);
        GLES20.glVertexAttribPointer(particleProgram.getStartPositionLoc(), 3, GLES20.GL_FLOAT,
                false, PARTICLE_SIZE * 4,
                mParticles);

        GLES20.glEnableVertexAttribArray(particleProgram.getLifetimeLoc());
        GLES20.glEnableVertexAttribArray(particleProgram.getEndPositionLoc());
        GLES20.glEnableVertexAttribArray(particleProgram.getStartPositionLoc());

        // Blend particles
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

        // Bind the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

        // Set the sampler texture unit to 0
        GLES20.glUniform1i(particleProgram.getSamplerLoc(), 0);

        // Drawing the actual points
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_PARTICLES);

        // Disable texture
        GLES20.glDisable(GLES20.GL_TEXTURE_2D);

        // Disable blend particles
        GLES20.glDisable(GLES20.GL_BLEND);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(particleProgram.getLifetimeLoc());
        GLES20.glDisableVertexAttribArray(particleProgram.getEndPositionLoc());
        GLES20.glDisableVertexAttribArray(particleProgram.getStartPositionLoc());
    }

    /**
     * Updates the time and position attributes of the particle system.
     *
     * @param particleProgram GL Program for particles
     */
    private void update(ParticleProgram particleProgram) {
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

            // set position
            GLES20.glUniform3f(particleProgram.getCenterPositionLoc(), 1f, 0f, 0f); // TODO

            // set color
            GLES20.glUniform4f(particleProgram.getColorLoc(), getShape().getColor()[0], getShape().getColor()[1], getShape().getColor()[2], .5f);
        }

        // Load uniform time variable
        GLES20.glUniform1f(particleProgram.getTimeLoc(), mTime);
    }

}
