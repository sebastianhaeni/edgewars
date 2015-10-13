package ch.sebastianhaeni.edgewars.graphics.programs;

import android.content.Context;
import android.opengl.GLES20;

public class ParticleProgram extends ESProgram {

    private static final String VERTEX_SHADER_SRC =
            "uniform float u_time;                                \n" +
                    "uniform vec3 u_centerPosition;                       \n" +
                    "attribute float a_lifetime;                          \n" +
                    "attribute vec3 a_startPosition;                      \n" +
                    "attribute vec3 a_endPosition;                        \n" +
                    "varying float v_lifetime;                            \n" +
                    "void main()                                          \n" +
                    "{                                                    \n" +
                    "  if ( u_time <= a_lifetime )                        \n" +
                    "  {                                                  \n" +
                    "    gl_Position.xyz = a_startPosition +              \n" +
                    "                      (u_time * a_endPosition);      \n" +
                    "    gl_Position.xyz += u_centerPosition;             \n" +
                    "    gl_Position.w = 1.0;                             \n" +
                    "  }                                                  \n" +
                    "  else                                               \n" +
                    "     gl_Position = vec4( -1000, -1000, 0, 0 );       \n" +
                    "  v_lifetime = 1.0 - ( u_time / a_lifetime );        \n" +
                    "  v_lifetime = clamp ( v_lifetime, 0.0, 1.0 );       \n" +
                    "  gl_PointSize = ( v_lifetime * v_lifetime ) * 40.0; \n" +
                    "}";

    private static final String FRAGMENT_SHADER_SRC =
            "precision mediump float;                             \n" +
                    "uniform vec4 u_color;                                \n" +
                    "varying float v_lifetime;                            \n" +
                    "uniform sampler2D s_texture;                         \n" +
                    "void main()                                          \n" +
                    "{                                                    \n" +
                    "  vec4 texColor;                                     \n" +
                    "  texColor = texture2D( s_texture, gl_PointCoord );  \n" +
                    "  gl_FragColor = vec4( u_color ) * texColor;         \n" +
                    "  gl_FragColor.a *= v_lifetime;                      \n" +
                    "}                                                    \n";

    // Attribute locations
    private int mLifetimeLoc;
    private int mStartPositionLoc;
    private int mEndPositionLoc;

    // Uniform location
    private int mTimeLoc;
    private int mColorLoc;
    private int mCenterPositionLoc;
    private int mSamplerLoc;

    public ParticleProgram(Context context) {
        super(context);

        // Get the attribute locations
        mLifetimeLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_lifetime");
        mStartPositionLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_startPosition");
        mEndPositionLoc = GLES20.glGetAttribLocation(getProgramHandle(), "a_endPosition");

        // Get the uniform locations
        mTimeLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_time");
        mCenterPositionLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_centerPosition");
        mColorLoc = GLES20.glGetUniformLocation(getProgramHandle(), "u_color");
        mSamplerLoc = GLES20.glGetUniformLocation(getProgramHandle(), "s_texture");
    }

    @Override
    protected String getVertexShaderSource() {
        return VERTEX_SHADER_SRC;
    }

    @Override
    protected String getFragmentShaderSource() {
        return FRAGMENT_SHADER_SRC;
    }

    public int getLifetimeLoc() {
        return mLifetimeLoc;
    }

    public int getEndPositionLoc() {
        return mEndPositionLoc;
    }

    public int getStartPositionLoc() {
        return mStartPositionLoc;
    }

    public int getSamplerLoc() {
        return mSamplerLoc;
    }

    public int getCenterPositionLoc() {
        return mCenterPositionLoc;
    }

    public int getColorLoc() {
        return mColorLoc;
    }

    public int getTimeLoc() {
        return mTimeLoc;
    }
}
