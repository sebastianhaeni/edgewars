package ch.sebastianhaeni.edgewars.graphics.drawables.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.programs.OpenGLUtil;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A text that can be drawn. Only supports a limited set of characters and no multiline or anything
 * fancy.
 */
public class Text extends Shape {

    // Icon characters
    public static final char PAUSE = '[';
    public static final char ENERGY = '\\';
    public static final char DAMAGE = ']';
    public static final char HEALTH = '^';
    public static final char WRENCH = '|';

    // future use:
    private static final char ACCURACY = '_';
    private static final char SPEED = '`';
    private static final char CANCEL = '{';

    /**
     * Width of a box of a single character.
     */
    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    private static final float RI_TEXT_WIDTH = 32.0f;
    private static final float RI_TEXT_SPACE_SIZE = 20f;

    private static final float UNIFORM_SCALE = .015f;

    private static final int[] LETTER_WIDTHS =
            {
                    36, 29, 30, 34, 25, 25, 34, 33,
                    11, 20, 31, 24, 48, 35, 39, 29,
                    42, 31, 27, 31, 34, 35, 46, 35,
                    31, 27, 30, 26, 28, 26, 31, 28,
                    28, 28, 29, 29, 14, 24, 30, 18,
                    26, 14, 14, 14, 25, 28, 31, 64,
                    64, 64, 64, 64, 64, 64, 64, 0,
                    0, 0, 0, 0, 0, 0, 0, 0
            };
    private final boolean mIsStatic;

    private String mText;

    @SuppressWarnings("FieldCanBeLocal")
    private transient FloatBuffer mVertexBuffer;
    @SuppressWarnings("FieldCanBeLocal")
    private transient FloatBuffer mTextureBuffer;
    @SuppressWarnings("FieldCanBeLocal")
    private transient FloatBuffer mColorBuffer;
    @SuppressWarnings("FieldCanBeLocal")
    private transient ShortBuffer mDrawListBuffer;

    private float[] mVectors;
    private float[] mTextureCoordinates;
    private short[] mIndices;
    private float[] mColors;

    private int mIndexVectors;
    private int mIndexIndices;
    private int mIndexTextureCoordinate;
    private int mIndexColors;
    private boolean mStaticPositionInitialized;

    /**
     * Constructor
     *
     * @param position position of the text
     * @param color    color of the text
     * @param text     the text itself
     * @param layer    draw layer
     */
    public Text(Position position, float[] color, String text, int layer) {
        this(position, color, text, layer, false);
    }

    /**
     * Constructor
     *
     * @param position position of the text
     * @param color    color of the text
     * @param text     the text itself
     * @param layer    draw layer
     * @param isStatic true if this text is not moved with the camera
     */
    public Text(Position position, float[] color, String text, int layer, boolean isStatic) {
        super(position, color, layer);
        mIsStatic = isStatic;

        setText(text);
    }

    /**
     * Sets the text and recalculates everything if the text has changed.
     *
     * @param text the new text
     */
    public void setText(String text) {
        if (mText != null && mText.equals(text)) {
            return;
        }

        mText = text;

        synchronized (this) {
            prepareDrawInfo();
            convertTextToTriangleInfo();
        }
    }

    @Override
    public void draw(GameRenderer renderer) {

        if (mIsStatic && !mStaticPositionInitialized) {

            if (renderer.getMaxGLX() <= 0) {
                // values not ready yet
                return;
            }

            setPosition(new Position(getPosition().getX() - renderer.getMaxGLX(), getPosition().getY() - renderer.getMaxGLY()));
            prepareDrawInfo();
            convertTextToTriangleInfo();
            mStaticPositionInitialized = true;
        }

        synchronized (this) {

            // Set the correct shader for our grid object.
            int programHandle = renderer.getTextProgram().getProgramHandle();
            GLES20.glUseProgram(programHandle);
            OpenGLUtil.checkGlError("glUseProgram");

            // The vertex buffer.
            ByteBuffer bb = ByteBuffer.allocateDirect(mVectors.length * 4);
            bb.order(ByteOrder.nativeOrder());
            mVertexBuffer = bb.asFloatBuffer();
            mVertexBuffer.put(mVectors);
            mVertexBuffer.position(0);

            // The vertex buffer.
            ByteBuffer bb3 = ByteBuffer.allocateDirect(mColors.length * 4);
            bb3.order(ByteOrder.nativeOrder());
            mColorBuffer = bb3.asFloatBuffer();
            mColorBuffer.put(mColors);
            mColorBuffer.position(0);

            // The texture buffer
            ByteBuffer bb2 = ByteBuffer.allocateDirect(mTextureCoordinates.length * 4);
            bb2.order(ByteOrder.nativeOrder());
            mTextureBuffer = bb2.asFloatBuffer();
            mTextureBuffer.put(mTextureCoordinates);
            mTextureBuffer.position(0);

            // initialize byte buffer for the draw list
            ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            mDrawListBuffer = dlb.asShortBuffer();
            mDrawListBuffer.put(mIndices);
            mDrawListBuffer.position(0);

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(renderer.getTextProgram().getPositionHandle());
            OpenGLUtil.checkGlError("glEnableVertexAttribArray");

            // Prepare the background coordinate data
            GLES20.glVertexAttribPointer(renderer.getTextProgram().getPositionHandle(), 3,
                    GLES20.GL_FLOAT, false,
                    0, mVertexBuffer);
            OpenGLUtil.checkGlError("glGetUniformLocation");

            // Prepare the texture coordinates
            GLES20.glVertexAttribPointer(renderer.getTextProgram().getTexCoordinateLoc(), 2, GLES20.GL_FLOAT,
                    false,
                    0, mTextureBuffer);
            OpenGLUtil.checkGlError("glVertexAttribPointer");

            GLES20.glEnableVertexAttribArray(renderer.getTextProgram().getPositionHandle());
            GLES20.glEnableVertexAttribArray(renderer.getTextProgram().getTexCoordinateLoc());
            GLES20.glEnableVertexAttribArray(renderer.getTextProgram().getColorHandle());
            OpenGLUtil.checkGlError("glEnableVertexAttribArray");

            // Prepare the background coordinate data
            GLES20.glVertexAttribPointer(renderer.getTextProgram().getColorHandle(), 4,
                    GLES20.GL_FLOAT, false,
                    0, mColorBuffer);
            OpenGLUtil.checkGlError("glVertexAttribPointer");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(renderer.getTextProgram().getMatrixHandle(), 1, false, mIsStatic
                    ? renderer.getStaticMVPMatrix()
                    : renderer.getMVPMatrix(), 0);
            OpenGLUtil.checkGlError("glUniformMatrix4fv");

            // Set the sampler texture unit to our selected id
            GLES20.glUniform1i(renderer.getTextProgram().getSamplerLoc(), GameRenderer.Textures.FONT.get());
            OpenGLUtil.checkGlError("glUniform1i");

            // draw the triangle
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndices.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
            OpenGLUtil.checkGlError("glDrawElements");

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(renderer.getTextProgram().getPositionHandle());
            GLES20.glDisableVertexAttribArray(renderer.getTextProgram().getTexCoordinateLoc());
            GLES20.glDisableVertexAttribArray(renderer.getTextProgram().getColorHandle());
        }
    }

    /**
     * Resets everything.
     */
    private void prepareDrawInfo() {
        // Reset the mIndices.
        mIndexVectors = 0;
        mIndexIndices = 0;
        mIndexTextureCoordinate = 0;
        mIndexColors = 0;

        // Get the total amount of characters
        int charCount = mText.length();

        // Create the arrays we need with the correct size.
        mVectors = null;
        mColors = null;
        mTextureCoordinates = null;
        mIndices = null;

        mVectors = new float[charCount * 12];
        mColors = new float[charCount * 16];
        mTextureCoordinates = new float[charCount * 8];
        mIndices = new short[charCount * 6];
    }

    /**
     * Converts the text to triangle info by selecting the characters from the font map and
     * applying in it to the data structures used to draw.
     */
    private void convertTextToTriangleInfo() {

        // Calculate width of whole text
        float textWidth = 0;
        for (int j = 0; j < mText.length(); j++) {
            char c = mText.charAt(j);
            int c_val = (int) c;

            int index = convertCharToIndex(c_val);
            if (index < 0) {
                textWidth += RI_TEXT_SPACE_SIZE * UNIFORM_SCALE;
            } else {
                textWidth += (LETTER_WIDTHS[index] / 2) * UNIFORM_SCALE;
            }
        }

        // Get attributes from text object
        float x = -(textWidth * .5f) + getPosition().getX();
        float y = .25f + getPosition().getY();

        // Create
        for (int j = 0; j < mText.length(); j++) {
            // get ascii value
            char c = mText.charAt(j);
            int c_val = (int) c;

            int index = convertCharToIndex(c_val);

            if (index == -1) {
                // unknown character, we will add a space for it to be save.
                x += RI_TEXT_SPACE_SIZE * UNIFORM_SCALE;
                continue;
            }

            // Calculate the uv parts
            int row = index / 8;
            int col = index % 8;

            float v = row * RI_TEXT_UV_BOX_WIDTH;
            float v2 = v + RI_TEXT_UV_BOX_WIDTH;
            float u = col * RI_TEXT_UV_BOX_WIDTH;
            float u2 = u + RI_TEXT_UV_BOX_WIDTH;

            // Creating the triangle information
            float[] vertices = new float[12];
            float[] textureCoordinates = new float[8];
            float[] colors;

            vertices[0] = x;
            vertices[1] = y + (RI_TEXT_WIDTH * -UNIFORM_SCALE);
            vertices[2] = 0;
            vertices[3] = x;
            vertices[4] = y;
            vertices[5] = 0;
            vertices[6] = x + (RI_TEXT_WIDTH * UNIFORM_SCALE);
            vertices[7] = y;
            vertices[8] = 0;
            vertices[9] = x + (RI_TEXT_WIDTH * UNIFORM_SCALE);
            vertices[10] = y + (RI_TEXT_WIDTH * -UNIFORM_SCALE);
            vertices[11] = 0;

            float[] color = getColor();

            colors = new float[]
                    {
                            color[0], color[1], color[2], color[3],
                            color[0], color[1], color[2], color[3],
                            color[0], color[1], color[2], color[3],
                            color[0], color[1], color[2], color[3]
                    };
            // 0.001f = texture bleeding hack/fix
            textureCoordinates[0] = u + 0.001f;
            textureCoordinates[1] = v + 0.001f;
            textureCoordinates[2] = u + 0.001f;
            textureCoordinates[3] = v2 - 0.001f;
            textureCoordinates[4] = u2 - 0.001f;
            textureCoordinates[5] = v2 - 0.001f;
            textureCoordinates[6] = u2 - 0.001f;
            textureCoordinates[7] = v + 0.001f;

            short[] indices = {0, 1, 2, 0, 2, 3};

            // Add our triangle information to our collection for 1 render call.
            addCharRenderInformation(vertices, colors, textureCoordinates, indices);

            // Calculate the new position
            x += ((LETTER_WIDTHS[index] / 2) * UNIFORM_SCALE);
        }
    }

    /**
     * Adds the generated information to the data structures that hold the data that's used to draw.
     *
     * @param vertices           vertices of the text
     * @param colors             the colors
     * @param textureCoordinates the texture coordinates
     * @param indices            the indices
     */
    private void addCharRenderInformation(float[] vertices, float[] colors, float[] textureCoordinates, short[] indices) {
        // We need a base value because the object has mIndices related to
        // that object and not to this collection so basically we need to
        // translate the mIndices to align with the vertex location in ou
        // mVectors array of mVectors.
        short base = (short) (mIndexVectors / 3);

        // We should add the vec, translating the indices to our saved vector
        for (float vertex : vertices) {
            mVectors[mIndexVectors] = vertex;
            mIndexVectors++;
        }

        // We should add the colors, so we can use the same texture for multiple effects.
        for (float color : colors) {
            mColors[mIndexColors] = color;
            mIndexColors++;
        }

        // We should add the uvs
        for (float texCoordinate : textureCoordinates) {
            mTextureCoordinates[mIndexTextureCoordinate] = texCoordinate;
            mIndexTextureCoordinate++;
        }

        // We handle the indices
        for (short anIndices : indices) {
            mIndices[mIndexIndices] = (short) (base + anIndices);
            mIndexIndices++;
        }
    }

    /**
     * Correlates the character index to the location in the font map.
     *
     * @param characterValue character casted to int
     * @return index for font map
     */
    private static int convertCharToIndex(int characterValue) {
        int index = -1;

        // Retrieve the index
        if (characterValue > 64 && characterValue < 91) { // A-Z
            index = characterValue - 65;
        } else if (characterValue > 96 && characterValue < 123) { // a-z
            index = characterValue - 97;
        } else if (characterValue > 47 && characterValue < 58) { // 0-9
            index = characterValue - 48 + 26;
        } else if (characterValue == 43) { // +
            index = 38;
        } else if (characterValue == 45) { // -
            index = 39;
        } else if (characterValue == 33) { // !
            index = 36;
        } else if (characterValue == 63) { // ?
            index = 37;
        } else if (characterValue == 61) { // =
            index = 40;
        } else if (characterValue == 58) { // :
            index = 41;
        } else if (characterValue == 46) { // .
            index = 42;
        } else if (characterValue == 44) { // ,
            index = 43;
        } else if (characterValue == 42) { // *
            index = 44;
        } else if (characterValue == 36) { // $
            index = 45;
        }

        switch ((char) characterValue) {
            case PAUSE:
                index = 47;
                break;
            case ENERGY:
                index = 48;
                break;
            case DAMAGE:
                index = 49;
                break;
            case HEALTH:
                index = 50;
                break;
            case ACCURACY:
                index = 51;
                break;
            case SPEED:
                index = 52;
                break;
            case CANCEL:
                index = 53;
                break;
            case WRENCH:
                index = 54;
                break;
        }

        return index;
    }

    /**
     * Recalculates the object to accommodate to new position.
     */
    public void calculateVertexBuffer() {
        synchronized (this) {
            prepareDrawInfo();
            convertTextToTriangleInfo();
        }
    }
}
