package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
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
    public static final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public static final int EYE_HEIGHT = 15;

    private final GameThread mThread;
    private final GameState mGameState;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;
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
            s.draw(this, mShapeProgram, mParticleProgram);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, EYE_HEIGHT);
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
     * @return gets the view matrix
     */
    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    /**
     * @return gets the game statae
     */
    public GameState getGameState() {
        return mGameState;
    }

    /**
     * @return gets the model view projection matrix
     */
    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    /**
     * @return gets the projection matrix
     */
    public float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }
}
