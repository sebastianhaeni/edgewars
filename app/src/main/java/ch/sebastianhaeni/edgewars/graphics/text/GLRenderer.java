package ch.sebastianhaeni.edgewars.graphics.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.sebastianhaeni.edgewars.R;

public class GLRenderer implements Renderer {

    // Our matrices
    private final float[] matrixProjection = new float[16];
    private final float[] matrixView = new float[16];
    private final float[] matrixProjectionAndView = new float[16];

    // Geometric variables
    private TextManager textManager;

    // Our screen resolution
    private float mScreenWidth = 1280;
    private float mScreenHeight = 768;
    private float ssu = 1.0f;
    private float ssx = 1.0f;
    private float ssy = 1.0f;
    private float swp = 320.0f;
    private float shp = 480.0f;

    // Misc
    private Context mContext;

    public GLRenderer(Context c) {
        mContext = c;
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // render the text
        if (textManager != null) {
            textManager.draw(matrixProjectionAndView);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            matrixProjection[i] = 0.0f;
            matrixView[i] = 0.0f;
            matrixProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(matrixProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(matrixView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(matrixProjectionAndView, 0, matrixProjection, 0, matrixView, 0);

        // Setup our scaling system
        setupScaling();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Setup our scaling system
        setupScaling();
        // Create the image information
        setupImage();
        // Create our texts
        setupText();

        // Set the clear color to black
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Text shader
        int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
        int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);

        riGraphicTools.sp_Text = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
        GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert);        // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_Text);                  // creates OpenGL ES program executables
    }

    public void setupText() {
        // Create our text manager
        textManager = new TextManager();

        // Tell our text manager to use index 1 of textures loaded
        textManager.setTextureID(1);

        // Pass the uniform scale
        textManager.setUniformScale(ssu);

        // Create our new text object
        TextObject txt = new TextObject("hello world", 200f, 10f);

        // Add it to our manager
        textManager.addText(txt);

        // Prepare the text for rendering
        textManager.prepareDraw();
    }

    public void setupScaling() {
        // The screen resolutions
        swp = mContext.getResources().getDisplayMetrics().widthPixels;
        shp = mContext.getResources().getDisplayMetrics().heightPixels;

        // Orientation is assumed landscape
        ssx = swp / 480.0f;
        ssy = shp / 320.0f;

        // Get our uniform scale
        if (ssx > ssy) {
            ssu = ssy;
        } else {
            ssu = ssx;
        }
    }

    public void setupImage() {
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

}
