package ch.sebastianhaeni.edgewars.graphics.text;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class TextManager {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
    private static final float RI_TEXT_WIDTH = 32.0f;
    private static final float RI_TEXT_SPACESIZE = 20f;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;

    private float[] vectors;
    private float[] uvs;
    private short[] indices;
    private float[] colors;

    private int index_vectors;
    private int index_indices;
    private int index_uvs;
    private int index_colors;

    private int textureNr;

    private float uniformScale;

    public static int[] l_size = {36, 29, 30, 34, 25, 25, 34, 33,
            11, 20, 31, 24, 48, 35, 39, 29,
            42, 31, 27, 31, 34, 35, 46, 35,
            31, 27, 30, 26, 28, 26, 31, 28,
            28, 28, 29, 29, 14, 24, 30, 18,
            26, 14, 14, 14, 25, 28, 31, 0,
            0, 38, 39, 12, 36, 34, 0, 0,
            0, 38, 0, 0, 0, 0, 0, 0};

    public Vector<TextObject> textCollection;

    public TextManager() {
        // Create our container
        textCollection = new Vector<>();

        // Create the arrays
        vectors = new float[3 * 10];
        colors = new float[4 * 10];
        uvs = new float[2 * 10];
        indices = new short[10];

        // init as 0 as default
        textureNr = 0;
    }

    public void addText(TextObject obj) {
        // Add text object to our collection
        textCollection.add(obj);
    }

    public void setTextureID(int val) {
        textureNr = val;
    }

    public void addCharRenderInformation(float[] vec, float[] cs, float[] uv, short[] indices) {
        // We need a base value because the object has indices related to
        // that object and not to this collection so basically we need to
        // translate the indices to align with the vertex location in ou
        // vectors array of vectors.
        short base = (short) (index_vectors / 3);

        // We should add the vec, translating the indices to our saved vector
        for (float aVec : vec) {
            vectors[index_vectors] = aVec;
            index_vectors++;
        }

        // We should add the colors, so we can use the same texture for multiple effects.
        for (float c : cs) {
            colors[index_colors] = c;
            index_colors++;
        }

        // We should add the uvs
        for (float anUv : uv) {
            uvs[index_uvs] = anUv;
            index_uvs++;
        }

        // We handle the indices
        for (short anIndices : indices) {
            this.indices[index_indices] = (short) (base + anIndices);
            index_indices++;
        }
    }

    public void PrepareDrawInfo() {
        // Reset the indices.
        index_vectors = 0;
        index_indices = 0;
        index_uvs = 0;
        index_colors = 0;

        // Get the total amount of characters
        int charCount = 0;
        for (TextObject txt : textCollection) {
            if (txt != null) {
                if (!(txt.text == null)) {
                    charCount += txt.text.length();
                }
            }
        }

        // Create the arrays we need with the correct size.
        vectors = null;
        colors = null;
        uvs = null;
        indices = null;

        vectors = new float[charCount * 12];
        colors = new float[charCount * 16];
        uvs = new float[charCount * 8];
        indices = new short[charCount * 6];

    }

    public void PrepareDraw() {
        // Setup all the arrays
        PrepareDrawInfo();

        // Using the iterator protects for problems with concurrency
        for (TextObject txt : textCollection) {
            if (txt != null) {
                if (!(txt.text == null)) {
                    convertTextToTriangleInfo(txt);
                }
            }
        }
    }

    public void Draw(float[] m) {
        // Set the correct shader for our grid object.
        GLES20.glUseProgram(riGraphicTools.sp_Text);

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vectors.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vectors);
        vertexBuffer.position(0);

        // The vertex buffer.
        ByteBuffer bb3 = ByteBuffer.allocateDirect(colors.length * 4);
        bb3.order(ByteOrder.nativeOrder());
        colorBuffer = bb3.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        // The texture buffer
        ByteBuffer bb2 = ByteBuffer.allocateDirect(uvs.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureBuffer = bb2.asFloatBuffer();
        textureBuffer.put(uvs);
        textureBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Text, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the background coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Text, "a_texCoord");

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, textureBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        int mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Text, "a_Color");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Prepare the background coordinate data
        GLES20.glVertexAttribPointer(mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer);

        // get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Text, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        int mSamplerLoc = GLES20.glGetUniformLocation(riGraphicTools.sp_Text, "s_texture");

        // Set the sampler texture unit to our selected id
        GLES20.glUniform1i(mSamplerLoc, textureNr);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }

    private int convertCharToIndex(int c_val) {
        int index = -1;

        // Retrieve the index
        if (c_val > 64 && c_val < 91) { // A-Z
            index = c_val - 65;
        } else if (c_val > 96 && c_val < 123) { // a-z
            index = c_val - 97;
        } else if (c_val > 47 && c_val < 58) { // 0-9
            index = c_val - 48 + 26;
        } else if (c_val == 43) { // +
            index = 38;
        } else if (c_val == 45) { // -
            index = 39;
        } else if (c_val == 33) { // !
            index = 36;
        } else if (c_val == 63) { // ?
            index = 37;
        } else if (c_val == 61) { // =
            index = 40;
        } else if (c_val == 58) { // :
            index = 41;
        } else if (c_val == 46) { // .
            index = 42;
        } else if (c_val == 44) { // ,
            index = 43;
        } else if (c_val == 42) { // *
            index = 44;
        } else if (c_val == 36) { // $
            index = 45;
        }

        return index;
    }

    private void convertTextToTriangleInfo(TextObject val) {
        // Get attributes from text object
        float x = val.x;
        float y = val.y;
        String text = val.text;

        // Create
        for (int j = 0; j < text.length(); j++) {
            // get ascii value
            char c = text.charAt(j);
            int c_val = (int) c;

            int index = convertCharToIndex(c_val);

            if (index == -1) {
                // unknown character, we will add a space for it to be save.
                x += ((RI_TEXT_SPACESIZE) * uniformScale);
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
            float[] vec = new float[12];
            float[] uv = new float[8];
            float[] colors;

            vec[0] = x;
            vec[1] = y + (RI_TEXT_WIDTH * uniformScale);
            vec[2] = 0.99f;
            vec[3] = x;
            vec[4] = y;
            vec[5] = 0.99f;
            vec[6] = x + (RI_TEXT_WIDTH * uniformScale);
            vec[7] = y;
            vec[8] = 0.99f;
            vec[9] = x + (RI_TEXT_WIDTH * uniformScale);
            vec[10] = y + (RI_TEXT_WIDTH * uniformScale);
            vec[11] = 0.99f;

            colors = new float[]
                    {val.color[0], val.color[1], val.color[2], val.color[3],
                            val.color[0], val.color[1], val.color[2], val.color[3],
                            val.color[0], val.color[1], val.color[2], val.color[3],
                            val.color[0], val.color[1], val.color[2], val.color[3]
                    };
            // 0.001f = texture bleeding hack/fix
            uv[0] = u + 0.001f;
            uv[1] = v + 0.001f;
            uv[2] = u + 0.001f;
            uv[3] = v2 - 0.001f;
            uv[4] = u2 - 0.001f;
            uv[5] = v2 - 0.001f;
            uv[6] = u2 - 0.001f;
            uv[7] = v + 0.001f;

            short[] indices = {0, 1, 2, 0, 2, 3};

            // Add our triangle information to our collection for 1 render call.
            addCharRenderInformation(vec, colors, uv, indices);

            // Calculate the new position
            x += ((l_size[index] / 2) * uniformScale);
        }
    }

    public void setUniformScale(float uniformScale) {
        this.uniformScale = uniformScale;
    }
}
