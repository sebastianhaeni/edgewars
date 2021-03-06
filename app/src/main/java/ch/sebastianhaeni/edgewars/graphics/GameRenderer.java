package ch.sebastianhaeni.edgewars.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.TextProgram;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.GameThread;
import ch.sebastianhaeni.edgewars.logic.entities.Button;
import ch.sebastianhaeni.edgewars.logic.entities.PauseButton;
import ch.sebastianhaeni.edgewars.ui.dialogs.PauseDialog;
import ch.sebastianhaeni.edgewars.util.Position;

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
    private static final int EYE_HEIGHT = 15;
    private static final int MIN_HEIGHT = 3;

    private final GameState mGameState;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mStaticMVPMatrix = new float[16];

    private final Context mContext;
    private final GameThread mThread;

    private int mScreenWidth;
    private int mScreenHeight;

    // mMaxX and mMaxY are the maximal values of our OpenGL coordinate system that are displayed on screen
    // the coordinate system ranges [-mMaxX, mMaxX] in X direction and [-mMaxY, mMaxY] in Y direction
    // note: Y axis is inverted; top-most value on screen is -mMaxY
    private float mMaxX;
    private float mMaxY;

    private ShapeProgram mShapeProgram;
    private ParticleProgram mParticleProgram;
    private TextProgram mTextProgram;
    private PauseButton mPauseButton;

    /**
     * Constructor
     *
     * @param context   app context
     * @param gameState game state containing all information about the game
     * @param thread    game thread
     */
    public GameRenderer(Context context, GameState gameState, GameThread thread) {
        mContext = context;
        mGameState = gameState;
        mThread = thread;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Create the image information
        loadTextures();

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mShapeProgram = new ShapeProgram();
        mParticleProgram = new ParticleProgram();
        mTextProgram = new TextProgram();

        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;

        float ratio = (float) width / height;

        mMaxX = ((float) EYE_HEIGHT / MIN_HEIGHT) * ratio;
        mMaxY = ((float) EYE_HEIGHT / MIN_HEIGHT);

        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, MIN_HEIGHT, EYE_HEIGHT);

        Matrix.setLookAtM(
                mViewMatrix,
                0,
                // eye point x,y,z
                0,
                0,
                -EYE_HEIGHT,
                // center of view x,y,z
                0,
                0,
                0f,
                // down vector x,y,z
                0f, -1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mStaticMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        createPauseButton();
    }

    /**
     * Creates the button to pause the game.
     */
    private void createPauseButton() {
        if (mPauseButton != null) {
            mPauseButton.unregister();
        }
        mPauseButton = new PauseButton(new Position((getGameCoordinateX(getMaxScreenX()) * 2) - 1, .5f));
        mPauseButton.register();
        mPauseButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mThread.onPause();
                PauseDialog dialog = new PauseDialog(mContext, mThread);
                dialog.show();
            }
        });
        mPauseButton.register();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        renderState();
    }

    /**
     * Renders the game state to the surface.
     */
    private void renderState() {
        Matrix.setLookAtM(
                mViewMatrix,
                0,
                // eye point x,y,z
                mGameState.getCamera().getX(),
                mGameState.getCamera().getY(),
                -EYE_HEIGHT,
                // center of view x,y,z
                mGameState.getCamera().getX(),
                mGameState.getCamera().getY(),
                0f,
                // down vector x,y,z
                0f, -1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // draw drawables
        for (Drawable s : Game.getInstance().getDrawables()) {
            s.draw(this);
        }
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

    public float getGameCoordinateX(float x) {
        return (x / (mScreenWidth / (2 * mMaxX))) - mMaxX;
    }

    public float getGameCoordinateY(float y) {
        return (y / (mScreenHeight / (2 * mMaxY))) - mMaxY;
    }

    /**
     * @return gets shape program
     */
    public ShapeProgram getShapeProgram() {
        return mShapeProgram;
    }

    /**
     * @return gets particle program
     */
    public ParticleProgram getParticleProgram() {
        return mParticleProgram;
    }

    /**
     * @return gets text program
     */
    public TextProgram getTextProgram() {
        return mTextProgram;
    }

    /**
     * Loads textures to the graphic memory.
     */
    private void loadTextures() {
        // Generate Textures, if more needed, alter these numbers.
        int[] textureNames = new int[1];
        GLES20.glGenTextures(1, textureNames, 0);

        // Again for the text texture
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.raw.font);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
    }

    /**
     * @return gets the static mvp matrix
     */
    public float[] getStaticMVPMatrix() {
        return mStaticMVPMatrix;
    }

    /**
     * @return gets max openGL X coordinate drawn on screen
     */
    public float getMaxGLX() {
        return mMaxX;
    }

    /**
     * @return gets max openGL Y coordinate drawn on screen
     */
    public float getMaxGLY() {
        return mMaxY;
    }

    /**
     * The Android X axis ranges from 0 (left) to pixel width of device (right)
     *
     * @return gets max Android X coordinate of screen (equals pixel width of device)
     */
    public float getMaxScreenX() {
        return mScreenWidth;
    }

    /**
     * The Android Y axis ranges from 0 (top) to pixel height of device (bottom)
     * <p/>
     * Note: the Android Y axis is inverted!
     *
     * @return gets max Android Y coordinate of screen (equals pixel height of device)
     */
    public float getMaxScreenY() {
        return mScreenHeight;
    }

    /**
     * Manages indexes for texture bitmaps. An index has to be unique.
     */
    public enum Textures {
        FONT(1);

        private final int mIndex;

        Textures(int index) {
            mIndex = index;
        }

        /**
         * @return gets the index
         */
        public int get() {
            return mIndex;
        }
    }
}
