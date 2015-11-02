package ch.sebastianhaeni.edgewars.logic.entities.board.node;

import android.databinding.Bindable;

import ch.sebastianhaeni.edgewars.BR;
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
    private static final int DAMAGE_LEVEL_UPGRADE_COST = 50;
    private static final int HEALTH_LEVEL_UPGRADE_COST = 50;
    private static final float RADIUS = .7f;
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
    private DeathParticleDecorator mParticles;
    //endregion

    /**
     * Constructor
     *
     * @param position the position this node is at
     */
    public Node(Position position) {
        mPosition = position;
        mHealth = getMaxHealth();

        mCircle = new Polygon(mPosition, Colors.NODE_NEUTRAL, 3, 80, 0, RADIUS);
        mHealthLabel = new TextDecorator(mCircle, String.valueOf(getHealth()), 6);

        setState(new NeutralState(this));
    }

    @Override
    public void update(long millis) {
        mState.update(millis);
    }

    @Override
    public void initialize() {
        mCircle.register();
        mHealthLabel.register();
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
        if (mHealthLevel >= 3) {
            return;
        }
        mHealthLevel++;
        repair();
        notifyPropertyChanged(BR.healthLevel);
    }

    /**
     * Upgrades the damage level to a max of 3.
     */
    public void upgradeDamage() {
        if (mDamageLevel >= 3) {
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
                new MeleeUnit(mMeleeUnits, node, ((OwnedState) this.getState()).getOwner()),
                node,
                Game.getInstance().getEdgeBetween(this, node)));
        mMeleeUnits = 0;
        notifyPropertyChanged(BR.node);
    }

    /**
     * Issues a command to send all tank units to another node from this node.
     *
     * @param node target node
     */
    public void sendTankUnits(Node node) {
        Game.getInstance().register(new MoveUnitCommand(
                new TankUnit(mTankUnits, node, ((OwnedState) this.getState()).getOwner()),
                node,
                Game.getInstance().getEdgeBetween(this, node)));
        mTankUnits = 0;
        notifyPropertyChanged(BR.node);
    }

    /**
     * Issues a command to send all sprinter units to another node from this node.
     *
     * @param node target node
     */
    public void sendSprinterUnits(Node node) {
        Game.getInstance().register(new MoveUnitCommand(
                new SprinterUnit(mSprinterUnits, node, ((OwnedState) this.getState()).getOwner()),
                node,
                Game.getInstance().getEdgeBetween(this, node)));
        mSprinterUnits = 0;
        notifyPropertyChanged(BR.node);
    }

    /**
     * Removes a damage value from health.
     *
     * @param attackDamage the amount of damage that is inflicted
     */
    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            if (getState() instanceof OwnedState && ((OwnedState) getState()).getOwner().isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_LOST);
            }

            setState(new NeutralState(this));
            mParticles = new DeathParticleDecorator(mCircle, 9);
            mHealth = 0;
            return;
        }
        mHealth = newHealth;
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
        return getMaxHealth() - getHealth() * 10;
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
                return 100;
            case 2:
                return 150;
            case 3:
                return 220;
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
        return mHealthLevel >= 3;
    }

    /**
     * @return gets if the maximum damage level has been reached
     */
    public boolean maxDamageLevelReached() {
        return mDamageLevel >= 3;
    }

    /**
     * @return gets the cost of upgrading the damage level
     */
    public int getDamageLevelUpgradeCost() {
        return DAMAGE_LEVEL_UPGRADE_COST;
    }

    /**
     * @return gets the cost of upgrading the health level
     */
    public int getHealthLevelUpgradeCost() {
        return HEALTH_LEVEL_UPGRADE_COST;
    }

    /**
     * @return gets the amount of damage the node inflicts to intruders
     */
    public int getDamage() {
        switch (mDamageLevel) {
            case 1:
                return 30;
            case 2:
                return 50;
            case 3:
                return 80;
            default:
                throw new IllegalArgumentException("Damage level " + mDamageLevel + " is not allowed");
        }
    }

    /**
     * @return gets the circle radius
     */
    public float getRadius() {
        return .7f;
    }

    //endregion

}
