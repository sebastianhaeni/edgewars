package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.GameThread;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 * <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class GameRenderer implements GLSurfaceView.Renderer {

    // number of coordinates per vertex in this array
    public static final int COORDS_PER_VERTEX = 3;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public static final int EYE_HEIGHT = 15;
    public static final int MIN_HEIGHT = 3;

    private final GameThread mThread;
    private final GameState mGameState;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    // mMaxX and mMaxY are the maximal values of our OpenGL coordinate system that are displayed on screen
    // the coordinate system ranges [-mMaxX, mMaxX] in X direction and [-mMaxY, mMaxY] in Y direction
    // note: Y axis is inverted; top-most value on screen is -mMaxY
    private float mMaxX;
    private float mMaxY;

    private ShapeProgram mShapeProgram;
    private ParticleProgram mParticleProgram;

    /**
     * Constructor
     *
     * @param context   app context
     * @param thread    game update thread
     * @param gameState game state containing all information about the game
     */
    public GameRenderer(Context context, GameThread thread, GameState gameState) {
        mContext = context;
        mThread = thread;
        mGameState = gameState;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mShapeProgram = new ShapeProgram(mContext);
        mParticleProgram = new ParticleProgram(mContext);

        Game.getInstance().reset();
        mThread.setRunning(true);
        mThread.start();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        renderState();
    }

    /**
     * Renders the game state to the surface.
     */
    private void renderState() {
        // Draw drawables
        for (IDrawable s : mGameState.getBoard().getDrawables()) {

            // Set the camera position (View matrix)
            Matrix.setLookAtM(
                    mViewMatrix,
                    0,
                    // eye point x,y,z
                    mGameState.getCamera().getX() + s.getShape().getPosition().getX(),
                    mGameState.getCamera().getY() + s.getShape().getPosition().getY(),
                    -EYE_HEIGHT,
                    // center of view x,y,z
                    mGameState.getCamera().getX() + s.getShape().getPosition().getX(),
                    mGameState.getCamera().getY() + s.getShape().getPosition().getY(),
                    0f,
                    // up vector x,y,z
                    0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            s.draw(this);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;

        mMaxX = ((float) EYE_HEIGHT / MIN_HEIGHT) * ((float) mScreenWidth / mScreenHeight);
        mMaxY = ((float) EYE_HEIGHT / MIN_HEIGHT);

        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, MIN_HEIGHT, EYE_HEIGHT);
    }

    /**
     * @return gets the screen width
     */
    public int getWidth() {
        return mScreenWidth;
    }

    /**
     * @return gets the screen height
     */
    public int getHeight() {
        return mScreenHeight;
    }

    /**
     * @return gets the model view projection matrix
     */
    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    /**
     * @param objectCoordinateX X coordinate of object (OpenGL)
     * @return the X coordinate in screen pixels
     */
    public float getAndroidCoordinateX(float objectCoordinateX) {
        return (objectCoordinateX + mMaxX) * (mScreenWidth / (2 * mMaxX));
    }

    /**
     * @param objectCoordinateY y coordinate of object (OpenGL)
     * @return the Y coordinate in screen pixels
     */
    public float getAndroidCoordinateY(float objectCoordinateY) {
        return (objectCoordinateY + mMaxY) * (mScreenHeight / (2 * mMaxY));
    }

    /**
     * @param objectLengthX length of object in X direction based on OpenGL coordinates
     * @return the length of object in X direction in screen pixels
     */
    public float getAndroidLengthX(float objectLengthX) {
        return (objectLengthX * (mScreenWidth) / (2 * mMaxX));
    }

    /**
     * @param objectLengthY length of object in Y direction based on OpenGL coordinates
     * @return the length of object in Y direction in screen pixels
     */
    public float getAndroidLengthY(float objectLengthY) {
        return (objectLengthY * (mScreenHeight) / (2 * mMaxY));
    }

    public ShapeProgram getShapeProgram() {
        return mShapeProgram;
    }

    public ParticleProgram getParticleProgram() {
        return mParticleProgram;
    }
}
