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
import ch.sebastianhaeni.edgewars.graphics.programs.ParticleProgram;
import ch.sebastianhaeni.edgewars.graphics.programs.ShapeProgram;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.graphics.text.TextManager;
import ch.sebastianhaeni.edgewars.graphics.text.TextObject;
import ch.sebastianhaeni.edgewars.graphics.text.riGraphicTools;
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

    private final GameThread mThread;
    private final GameState mGameState;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private TextManager mTextManager;

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
        // Create the image information
        setupImage();
        // Create our texts
        setupText();

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mShapeProgram = new ShapeProgram(mContext);
        mParticleProgram = new ParticleProgram(mContext);

        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Text shader
        int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
        int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);

        riGraphicTools.sp_Text = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
        GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert);        // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_Text);                  // creates OpenGL ES program executables

        mThread.setRunning(true);
        mThread.start();
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

    @Override
    public void onDrawFrame(GL10 unused) {
        // draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        renderState();

        // render the text
        if (mTextManager != null) {
            mTextManager.draw(mMVPMatrix);
        }
    }

    /**
     * Renders the game state to the surface.
     */
    private void renderState() {
        // draw drawables
        for (IDrawable s : Game.getInstance().getDrawables()) {

            // Set the camera position (View matrix)
            Matrix.setLookAtM(
                    mViewMatrix,
                    0,
                    mGameState.getCamera().getX() + s.getShape().getPosition().getX(),
                    mGameState.getCamera().getY() + s.getShape().getPosition().getY(),
                    -EYE_HEIGHT,
                    mGameState.getCamera().getX() + s.getShape().getPosition().getX(),
                    mGameState.getCamera().getY() + s.getShape().getPosition().getY(),
                    0f, 0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            s.draw(this);
        }
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

    private void setupImage() {
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

    private void setupText() {
        // Create our text manager
        mTextManager = new TextManager();

        // Tell our text manager to use index 1 of textures loaded
        mTextManager.setTextureID(1);

        // Pass the uniform scale
        mTextManager.setUniformScale(.025f);

        // Create our new text object
        TextObject txt = new TextObject("102 Energy", 1f, 2f);

        // Add it to our manager
        mTextManager.addText(txt);

        // Prepare the text for rendering
        mTextManager.prepareDraw();
    }

    public TextManager getTextManager() {
        return mTextManager;
    }
}
