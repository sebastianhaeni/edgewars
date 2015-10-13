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
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;

public class DeathParticleDecorator extends DrawableDecorator {
    private final int PARTICLE_SIZE = 7;
    private final int NUM_PARTICLES = 1000;

    // Texture handle
    private int mTextureId;

    // Update time
    private float mTime;
    private long mLastTime;

    private FloatBuffer mParticles;

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

        // Load particle texture
        mTextureId = loadTexture(GameApplication.getAppContext().getResources().openRawResource(R.raw.smoke));
    }

    /**
     * Load texture from resource.
     *
     * @param is stream of texture
     * @return handle to the texture
     */
    private int loadTexture(InputStream is) {
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

        return textureId[0];
    }

    @Override
    public void draw(ShapeProgram shapeProgram, ParticleProgram particleProgram) {
        update(particleProgram);

        // Use the program object
        GLES20.glUseProgram(particleProgram.getProgramHandle());

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

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_PARTICLES);
    }

    private void update(ParticleProgram particleProgram) {
        if (mLastTime == 0)
            mLastTime = SystemClock.uptimeMillis();
        long curTime = SystemClock.uptimeMillis();
        long elapsedTime = curTime - mLastTime;
        float deltaTime = elapsedTime / 1000.0f;
        mLastTime = curTime;

        mTime += deltaTime;

        if (mTime >= 1.0f) {
            Random generator = new Random();
            float[] centerPos = new float[3];
            float[] color = new float[4];

            mTime = 0.0f;

            // Pick a new start location and color
            centerPos[0] = generator.nextFloat() * 1.0f - 0.5f;
            centerPos[1] = generator.nextFloat() * 1.0f - 0.5f;
            centerPos[2] = generator.nextFloat() * 1.0f - 0.5f;

            GLES20.glUniform3f(particleProgram.getCenterPositionLoc(), centerPos[0], centerPos[1], centerPos[2]);

            // Random color
            color[0] = generator.nextFloat() * 0.5f + 0.5f;
            color[1] = generator.nextFloat() * 0.5f + 0.5f;
            color[2] = generator.nextFloat() * 0.5f + 0.5f;
            color[3] = 0.5f;

            GLES20.glUniform4f(particleProgram.getColorLoc(), color[0], color[1], color[2], color[3]);
        }

        // Load uniform time variable
        GLES20.glUniform1f(particleProgram.getTimeLoc(), mTime);
    }

}