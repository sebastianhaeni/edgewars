package ch.sebastianhaeni.edgewars.logic;

/**
 * Constants for the game.
 */
public final class Constants {

    //region player
    public static final long PLAYER_UPDATE_INTERVAL = 1000;
    //endregion

    //region AI rules
    public static final long ATTACK_RULE_UPDATE_INTERVAL = 4000;
    public static final long BACKUP_RULE_UPDATE_INTERVAL = 4000;
    public static final long BUILDUP_RULE_UPDATE_INTERVAL = 4000;
    public static final long CONQUER_RULE_UPDATE_INTERVAL = 4000;
    public static final long DEFENSE_RULE_UPDATE_INTERVAL = 4000;
    public static final long REPAIR_RULE_UPDATE_INTERVAL = 4000;
    //endregion

    //region camera
    public static final float CAMERA_FRICTION = .1f;
    public static final float CAMERA_TOUCH_SCALE_FACTOR = -.006f;
    public static final float CAMERA_PRECISION = .001f;
    public static final float CAMERA_SENSITIVITY = 5f;
    //endregion

    //region layers
    public static final int NODE_LAYER = 3;
    public static final int UNIT_SHAPE_LAYER = 6;
    public static final int UNIT_TEXT_LAYER = 7;
    public static final int DEATH_PARTICLE_LAYER = 9;
    //endregion

    //region factories
    public static final int FACTORY_MAX_LEVEL = 3;
    public static final int FACTORY_MELEE_UPGRADE_1 = 50;
    public static final int FACTORY_MELEE_UPGRADE_2 = 60;
    public static final int FACTORY_MELEE_UPGRADE_3 = 80;
    public static final int FACTORY_MELEE_UNIT_COST = 10;
    public static final int FACTORY_MELEE_PRODUCING_DURATION_1 = 2000;
    public static final int FACTORY_MELEE_PRODUCING_DURATION_2 = 1000;
    public static final int FACTORY_MELEE_PRODUCING_DURATION_3 = 400;

    public static final int FACTORY_SPRINTER_UPGRADE_1 = 50;
    public static final int FACTORY_SPRINTER_UPGRADE_2 = 60;
    public static final int FACTORY_SPRINTER_UPGRADE_3 = 80;
    public static final int FACTORY_SPRINTER_UNIT_COST = 10;
    public static final int FACTORY_SPRINTER_PRODUCING_DURATION_1 = 2000;
    public static final int FACTORY_SPRINTER_PRODUCING_DURATION_2 = 1000;
    public static final int FACTORY_SPRINTER_PRODUCING_DURATION_3 = 400;

    public static final int FACTORY_TANK_UPGRADE_1 = 50;
    public static final int FACTORY_TANK_UPGRADE_2 = 60;
    public static final int FACTORY_TANK_UPGRADE_3 = 80;
    public static final int FACTORY_TANK_UNIT_COST = 10;
    public static final int FACTORY_TANK_PRODUCING_DURATION_1 = 2000;
    public static final int FACTORY_TANK_PRODUCING_DURATION_2 = 1000;
    public static final int FACTORY_TANK_PRODUCING_DURATION_3 = 400;
    //endregion

    //region nodes
    public static final float NODE_RADIUS = .7f;
    public static final int NODE_DAMAGE_LEVEL_UPGRADE_COST = 50;
    public static final int NODE_HEALTH_LEVEL_UPGRADE_COST = 50;
    public static final int NODE_CORNERS = 120;
    public static final int NODE_HEALTH_MAX_LEVEL = 3;
    public static final int NODE_DAMAGE_MAX_LEVEL = 3;
    public static final int NODE_REPAIR_COST_MULTIPLIER = 10;
    public static final int NODE_MAX_HEALTH_1 = 100;
    public static final int NODE_MAX_HEALTH_2 = 150;
    public static final int NODE_MAX_HEALTH_3 = 300;
    public static final int NODE_DAMAGE_1 = 30;
    public static final int NODE_DAMAGE_2 = 50;
    public static final int NODE_DAMAGE_3 = 70;
    public static final float EDGE_WIDTH = .2f;
    //endregion

    //region node menu
    public static final int MENU_BUTTON_LAYER = 10;
    public static final int MENU_BUTTON_TEXT_LAYER = 11;
    public static final float MENU_BUTTON_RADIUS = .35f;
    public static final float MENU_BUTTON_LINE_WIDTH = .1f;
    //endregion

    //region node states
    public static final int OWNED_STATE_HEALTH_GAIN = 2;
    public static final int OWNED_STATE_ENERGY_GAIN = 10;
    public static final int OWNED_STATE_UPDATE_INTERVAL = 1000;
    //endregion

    //region units
    public static final int UNIT_UPDATE_INTERVAL = 10;
    public static final float UNIT_RADIUS = .5f;
    public static final float UNIT_SPEED_DIVISOR = 2000f;

    public static final int UNIT_MELEE_ATTACK_DAMAGE = 5;
    public static final int UNIT_MELEE_HEALTH = 5;
    public static final int UNIT_MELEE_ACCURACY = 1;
    public static final int UNIT_MELEE_SPEED = 55;
    public static final int UNIT_MELEE_CORNERS = 3;

    public static final int UNIT_SPRINTER_ATTACK_DAMAGE = 5;
    public static final int UNIT_SPRINTER_HEALTH = 5;
    public static final int UNIT_SPRINTER_ACCURACY = 1;
    public static final int UNIT_SPRINTER_SPEED = 55;
    public static final int UNIT_SPRINTER_CORNERS = 4;

    public static final int UNIT_TANK_ATTACK_DAMAGE = 5;
    public static final int UNIT_TANK_HEALTH = 5;
    public static final int UNIT_TANK_ACCURACY = 1;
    public static final int UNIT_TANK_SPEED = 55;
    public static final int UNIT_TANK_CORNERS = 5;

    public static final int ATTACK_NODE_UPDATE_INTERVAL = 70;
    public static final int FIGHT_UNIT_STATE_UPDATE_INTERVAL = 200;
    public static final int MOVING_STATE_UPDATE_INTERVAL = 10;
    public static final int WAIT_STATE_UPDATE_INTERVAL = 200;
    public static final float UNIT_NODE_ATTACK_DISTANCE = .2f;
    public static final float UNIT_FIGHT_DISTANCE = .5f;
    //endregion
}
