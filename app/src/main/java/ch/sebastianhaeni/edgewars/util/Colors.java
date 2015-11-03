package ch.sebastianhaeni.edgewars.util;

/**
 * Colors used in the game for all the different kinds of reasons.
 */
public abstract class Colors {

    // Node colors
    public static final float[] NODE_NEUTRAL = {.4f, .4f, .4f, 1f};
    public static final float[] NODE_MINE = {0.13671875f, 0.16953125f, 0.82265625f, 1f};
    public static final float[] NODE_OPPONENT = {0.63671875f, 0.16953125f, 0.22265625f, 1f};

    // Line color
    public static final float[] EDGE = {.3f, .3f, .3f, 1f};

    // Text
    public static final float[] TEXT = {1f, 1f, 1f, 1f};

    // Node selection corona
    public static final float[] CORONA = {1f, 1f, 1f, 1f};

    // Energy label that display the players energy
    public static final float[] ENERGY_TEXT = {1f, 1f, 1f, 1f};
}
