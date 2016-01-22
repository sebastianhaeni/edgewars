package ch.sebastianhaeni.edgewars.util;

/**
 * Colors used in the game for all the different kinds of reasons.
 */
public interface Colors {

    // Node colors
    float[] NODE_NEUTRAL = {.4f, .4f, .4f, 1f};
    float[] NODE_MINE = {0.13671875f, 0.16953125f, 0.82265625f, 1f};
    float[] NODE_OPPONENT = {0.63671875f, 0.16953125f, 0.22265625f, 1f};

    // Line color
    float[] EDGE = {.3f, .3f, .3f, 1f};

    // Text
    float[] TEXT = {1f, 1f, 1f, 1f};

    // Node selection corona
    float[] CORONA = {1f, 1f, 1f, 1f};
    float[] CORONA_DROP = {97f, 189f, 79f, 1f};

    // Energy label that display the players energy
    float[] ENERGY_TEXT = {1f, 1f, 1f, 1f};
    float[] ENERGY_DEDUCTION = {1f, .1f, .1f, 1f};
}
