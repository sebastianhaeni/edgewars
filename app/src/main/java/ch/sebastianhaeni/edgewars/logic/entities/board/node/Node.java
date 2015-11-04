package ch.sebastianhaeni.edgewars.logic.entities.board.node;

import android.databinding.Bindable;
import android.util.Log;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.DeathParticleDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.TankFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A node. It has the following:
 * <ul>
 * <li>state</li>
 * <li>units (melee, tank, sprinter)</li>
 * <li>factories (melee, tank, sprinter)</li>
 * <li>health</li>
 * <li>health level</li>
 * <li>damage level</li>
 * <li>position</li>
 * <li>drawables that represent this node graphically</li>
 * <li>scheduled commands to move units to another node</li>
 * </ul>
 */
public class Node extends BoardEntity {

    //region members
    private final Polygon mCircle;
    private final TextDecorator mHealthLabel;
    private int mMeleeUnits;
    private int mTankUnits;
    private int mSprinterUnits;

    private final MeleeFactory mMeleeFactory = new MeleeFactory(this);
    private final TankFactory mTankFactory = new TankFactory(this);
    private final SprinterFactory mSprinterFactory = new SprinterFactory(this);

    private int mHealth;
    private int mHealthLevel = 1;
    private int mDamageLevel = 1;
    private final Position mPosition;

    private NodeState mState;
    //endregion

    /**
     * Constructor
     *
     * @param position the position this node is at
     */
    public Node(Position position) {
        super();

        mPosition = position;
        mHealth = getMaxHealth();

        mCircle = new Polygon(mPosition, Colors.NODE_NEUTRAL, NODE_LAYER, NODE_CORNERS, 0, NODE_RADIUS);
        mHealthLabel = new TextDecorator(mCircle, String.valueOf(getHealth()), 6);

        setState(new NeutralState(this));
    }

    @Override
    public void update(long millis) {
        mState.update(millis);
    }

    @Override
    public void register() {
        super.register();
        mCircle.register();
        mHealthLabel.register();

        mMeleeFactory.register();
        mTankFactory.register();
        mSprinterFactory.register();
    }

    //region actions

    /**
     * Adds a melee unit to this node.
     */
    public void addMeleeUnit() {
        mMeleeUnits++;
        notifyPropertyChanged(BR.meleeCount);
    }

    /**
     * Adds a tank unit to this node.
     */
    public void addTankUnit() {
        mTankUnits++;
        notifyPropertyChanged(BR.tankCount);
    }

    /**
     * Adds a sprinter unit to this node.
     */
    public void addSprinterUnit() {
        mSprinterUnits++;
        notifyPropertyChanged(BR.sprinterCount);
    }

    /**
     * Adds units to a node.
     *
     * @param unit the unit to be added
     */
    public void addUnit(Unit unit) {
        if (unit instanceof MeleeUnit) {
            mMeleeUnits += unit.getCount();
            return;
        }
        if (unit instanceof TankUnit) {
            mTankUnits += unit.getCount();
            return;
        }
        if (unit instanceof SprinterUnit) {
            mSprinterUnits += unit.getCount();
            return;
        }
        throw new IllegalArgumentException("Unit is not handled.");
    }

    /**
     * Upgrades the health level to a max of 3.
     */
    public void upgradeHealth() {
        if (mHealthLevel >= NODE_HEALTH_MAX_LEVEL) {
            return;
        }
        mHealthLevel++;
        notifyPropertyChanged(BR.healthLevel);
    }

    /**
     * Upgrades the damage level to a max of 3.
     */
    public void upgradeDamage() {
        if (mDamageLevel >= NODE_DAMAGE_MAX_LEVEL) {
            return;
        }
        mDamageLevel++;
        notifyPropertyChanged(BR.damageLevel);
    }

    /**
     * Sets the health to full again.
     */
    public void repair() {
        mHealth = getMaxHealth();
        mHealthLabel.setText(String.valueOf(mHealth));
        notifyPropertyChanged(BR.health);
    }

    /**
     * Issues a command to send all melee units to another node from this node.
     *
     * @param node target node
     */
    public void sendMeleeUnits(Node node) {
        Game.getInstance().register(new MoveUnitCommand(
                mMeleeUnits,
                EUnitType.MELEE,
                node,
                Game.getInstance().getEdgeBetween(this, node),
                ((OwnedState) getState()).getOwner()));
    }

    /**
     * Issues a command to send all tank units to another node from this node.
     *
     * @param node target node
     */
    public void sendTankUnits(Node node) {
        Game.getInstance().register(new MoveUnitCommand(
                mTankUnits,
                EUnitType.TANK,
                node,
                Game.getInstance().getEdgeBetween(this, node),
                ((OwnedState) getState()).getOwner()));
    }

    /**
     * Issues a command to send all sprinter units to another node from this node.
     *
     * @param node target node
     */
    public void sendSprinterUnits(Node node) {
        Game.getInstance().register(new MoveUnitCommand(
                mSprinterUnits,
                EUnitType.SPRINTER,
                node,
                Game.getInstance().getEdgeBetween(this, node),
                ((OwnedState) getState()).getOwner()));
    }

    /**
     * Clears the unit count.
     *
     * @param unit the unit to be cleared
     */
    public void clearUnit(Unit unit) {
        if (unit instanceof MeleeUnit) {
            mMeleeUnits = 0;
            notifyPropertyChanged(BR.meleeCount);
            return;
        }
        if (unit instanceof TankUnit) {
            mTankUnits = 0;
            notifyPropertyChanged(BR.tankCount);
            return;
        }
        if (unit instanceof SprinterUnit) {
            mSprinterUnits = 0;
            notifyPropertyChanged(BR.sprinterCount);
        }
    }

    /**
     * Resets the node by removing all units and resetting the levels.
     */
    public void clearUnitsAndLevels() {
        mDamageLevel = 1;
        mHealthLevel = 1;
        mMeleeUnits = 0;
        mSprinterUnits = 0;
        mTankUnits = 0;
    }

    /**
     * Removes a damage value from health.
     *
     * @param attackDamage the amount of damage that is inflicted
     */
    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            Log.d("Node", "Node died!");
            if (getState() instanceof OwnedState && ((OwnedState) getState()).getOwner().isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_LOST);
            }

            setState(new NeutralState(this));
            DeathParticleDecorator particles = new DeathParticleDecorator(mCircle, 9);
            particles.register();

            mHealth = 0;
        } else {
            mHealth = newHealth;
        }

        mHealthLabel.setText(String.valueOf(mHealth));
        notifyPropertyChanged(BR.health);
        notifyPropertyChanged(BR.repairCost);
    }

    /**
     * Adds health to the node.
     *
     * @param healthGain amount of health gained
     */
    public void addHealth(int healthGain) {
        if (mHealth + healthGain > getMaxHealth()) {
            mHealth = getMaxHealth();
        } else {
            mHealth += healthGain;
        }

        mHealthLabel.setText(String.valueOf(mHealth));
        notifyPropertyChanged(BR.health);
        notifyPropertyChanged(BR.repairCost);
    }

    //endregion

    //region databinding

    /**
     * @return gets the cost to repair the node with the current health
     */
    @Bindable
    public int getRepairCost() {
        return (getMaxHealth() - getHealth()) * NODE_REPAIR_COST_MULTIPLIER;
    }

    /**
     * @return gets the current health
     */
    @Bindable
    public int getHealth() {
        return mHealth;
    }

    /**
     * @return gets the current maximum of health (depends on health level)
     */
    @Bindable
    public int getMaxHealth() {
        switch (mHealthLevel) {
            case 1:
                return NODE_MAX_HEALTH_1;
            case 2:
                return NODE_MAX_HEALTH_2;
            case 3:
                return NODE_MAX_HEALTH_3;
            default:
                throw new IllegalStateException("Level must be 1, 2 or 3");
        }
    }

    /**
     * @return gets health level
     */
    @Bindable
    public int getHealthLevel() {
        return mHealthLevel;
    }

    /**
     * @return gets damage level
     */
    @Bindable
    public int getDamageLevel() {
        return mDamageLevel;
    }

    /**
     * @return gets melee unit count
     */
    @Bindable
    public int getMeleeCount() {
        return mMeleeUnits;
    }

    /**
     * @return gets tank unit count
     */
    @Bindable
    public int getTankCount() {
        return mTankUnits;
    }

    /**
     * @return gets sprinter unit count
     */
    @Bindable
    public int getSprinterCount() {
        return mSprinterUnits;
    }

    //endregion

    //region getters/setters

    /**
     * @return gets the circle representing this node
     */
    public Shape getCircle() {
        return mCircle;
    }

    /**
     * @return gets the node's position
     */
    public Position getPosition() {
        return mPosition;
    }

    /**
     * @return gets melee factory
     */
    public MeleeFactory getMeleeFactory() {
        return mMeleeFactory;
    }

    /**
     * @return gets tank factory
     */
    public TankFactory getTankFactory() {
        return mTankFactory;
    }

    /**
     * @return gets sprinter factory
     */
    public SprinterFactory getSprinterFactory() {
        return mSprinterFactory;
    }

    /**
     * Sets the color of this node.
     *
     * @param color new color
     */
    public void setColor(float[] color) {
        mCircle.setColor(color);
    }

    /**
     * @return gets the node's state
     */
    public NodeState getState() {
        return mState;
    }

    /**
     * Sets the new state of this node.
     *
     * @param state new state
     */
    public void setState(NodeState state) {
        mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    /**
     * @return gets if the maximum health level has been reached
     */
    public boolean maxHealthLevelReached() {
        return mHealthLevel >= NODE_HEALTH_MAX_LEVEL;
    }

    /**
     * @return gets if the maximum damage level has been reached
     */
    public boolean maxDamageLevelReached() {
        return mDamageLevel >= NODE_DAMAGE_MAX_LEVEL;
    }

    /**
     * @return gets the amount of damage the node inflicts to intruders
     */
    public int getDamage() {
        switch (mDamageLevel) {
            case 1:
                return NODE_DAMAGE_1;
            case 2:
                return NODE_DAMAGE_2;
            case 3:
                return NODE_DAMAGE_3;
            default:
                throw new IllegalArgumentException("Damage level " + mDamageLevel + " is not allowed");
        }
    }

    //endregion

}
