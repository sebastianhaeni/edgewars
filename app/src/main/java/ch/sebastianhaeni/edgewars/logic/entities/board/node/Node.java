package ch.sebastianhaeni.edgewars.logic.entities.board.node;

import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.Stack;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.graphics.shapes.Circle;
import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.TankFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
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

    private final Circle mCircle;
    private ArrayList<MeleeUnit> mMeleeUnits = new ArrayList<>();
    private ArrayList<TankUnit> mTankUnits = new ArrayList<>();
    private ArrayList<SprinterUnit> mSprinterUnits = new ArrayList<>();

    private MeleeFactory mMeleeFactory = new MeleeFactory(this);
    private TankFactory mTankFactory = new TankFactory(this);
    private SprinterFactory mSprinterFactory = new SprinterFactory(this);

    private int mHealth;
    private int mHealthLevel = 1;
    private int mDamageLevel = 1;
    private Position mPosition;
    private ArrayList<IDrawable> mDrawables = new ArrayList<>();

    private Stack<MoveUnitCommand> mMoveUnitCommands = new Stack<>();
    private NodeState mState;

    /**
     * Constructor
     *
     * @param position the position this node is at
     */
    public Node(Position position) {
        super(50);
        setState(new NeutralState(this));
        mPosition = position;
        mHealth = getMaxHealth();

        mCircle = new Circle(mPosition);

        mDrawables.add(mCircle);
       // mDrawables.add(new DeathParticleDecorator(mCircle));
    }

    @Override
    public void update(long millis) {
        if (mMoveUnitCommands.size() > 0) {
            MoveUnitCommand c = mMoveUnitCommands.pop();
            Game.getInstance().register(c);
        }

        mState.update(millis);
    }

    /**
     * @return gets the node's position
     */
    public Position getPosition() {
        return mPosition;
    }

    @Override
    public ArrayList<IDrawable> getDrawables() {
        return mDrawables;
    }

    /**
     * Adds a melee unit to this node.
     *
     * @param unit unit to be added
     */
    public void addUnit(MeleeUnit unit) {
        mMeleeUnits.add(unit);
        notifyPropertyChanged(BR.meleeCount);
    }

    /**
     * Adds a tank unit to this node.
     *
     * @param unit unit to be added
     */
    public void addUnit(TankUnit unit) {
        mTankUnits.add(unit);
        notifyPropertyChanged(BR.tankCount);
    }

    /**
     * Adds a sprinter unit to this node.
     *
     * @param unit unit to be added
     */
    public void addUnit(SprinterUnit unit) {
        mSprinterUnits.add(unit);
        notifyPropertyChanged(BR.sprinterCount);
    }

    /**
     * Adds a unit to this node. It is figured out if it is a melee, tank or sprinter.
     *
     * @param unit unit to be added
     */
    public void addUnit(Unit unit) {
        if (unit instanceof MeleeUnit) {
            mMeleeUnits.add((MeleeUnit) unit);
            return;
        }
        if (unit instanceof TankUnit) {
            mTankUnits.add((TankUnit) unit);
            return;
        }
        if (unit instanceof SprinterUnit) {
            mSprinterUnits.add((SprinterUnit) unit);
        }
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
        notifyPropertyChanged(BR.health);
    }

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
        return mMeleeUnits.size();
    }

    /**
     * @return gets tank unit count
     */
    @Bindable
    public int getTankCount() {
        return mTankUnits.size();
    }

    /**
     * @return gets sprinter unit count
     */
    @Bindable
    public int getSprinterCount() {
        return mSprinterUnits.size();
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
     * Issues a command to send all melee units to another node from this node.
     *
     * @param node target node
     */
    public void sendMeleeUnits(Node node) {
        for (Unit u : mMeleeUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
        notifyPropertyChanged(BR.node);
    }

    /**
     * Issues a command to send all tank units to another node from this node.
     *
     * @param node target node
     */
    public void sendTankUnits(Node node) {
        for (Unit u : mTankUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
        notifyPropertyChanged(BR.node);
    }

    /**
     * Issues a command to send all sprinter units to another node from this node.
     *
     * @param node target node
     */
    public void sendSprinterUnits(Node node) {
        for (Unit u : mSprinterUnits) {
            mMoveUnitCommands.add(new MoveUnitCommand(u, node));
        }
        mMeleeUnits.clear();
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
            setState(new NeutralState(this));
            // TODO add death particles
            mHealth = 0;
            return;
        }
        mHealth = newHealth;

        notifyPropertyChanged(BR.health);
        notifyPropertyChanged(BR.repairCost);
    }

    /**
     * Sets the new state of this node.
     * @param state new state
     */
    public void setState(NodeState state) {
        mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    /**
     * Sets the color of this node.
     * @param color new color
     */
    public void setColor(float[] color) {
        for (IDrawable s : getDrawables()) {
            s.getShape().setColor(color);
        }
    }

    /**
     * @return gets the node's state
     */
    public NodeState getState() {
        return mState;
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
        return 50;
    }

    /**
     * @return gets the cost of upgrading the health level
     */
    public int getHealthLevelUpgradeCost() {
        return 50;
    }
}
