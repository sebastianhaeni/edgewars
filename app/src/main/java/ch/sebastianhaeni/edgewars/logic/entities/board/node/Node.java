package ch.sebastianhaeni.edgewars.logic.entities.board.node;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.DeathParticleDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.MeleeFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.SprinterFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.TankFactory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.menu.NodeMenu;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;
import ch.sebastianhaeni.edgewars.ui.IClickable;
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
public class Node extends BoardEntity implements IClickable {

    //region members
    private static NodeMenu mNodeMenu;
    private static boolean mSelectingNode;
    private static Node mSourceNode;
    private static EUnitType mSendingUnitType;
    private static final ArrayList<Shape> mCoronas = new ArrayList<>();

    private final Polygon mCircle;
    private final TextDecorator mHealthLabel;
    private int mMeleeUnits;
    private int mTankUnits;
    private int mSprinterUnits;
    private int mMeleeHealth = Constants.UNIT_MELEE_HEALTH;
    private int mTankHealth = Constants.UNIT_TANK_HEALTH;
    private int mSprinterHealth = Constants.UNIT_SPRINTER_HEALTH;

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

        mCircle = new Polygon(mPosition, Colors.NODE_NEUTRAL, Constants.NODE_LAYER, Constants.NODE_CORNERS, 0, Constants.NODE_RADIUS);
        mHealthLabel = new TextDecorator(mCircle, String.valueOf(getHealth()) + Text.HEALTH, 6);

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

    @Override
    public String toString() {
        return super.toString() + "[x=" + mPosition.getX() + ", y=" + mPosition.getY() + "]";
    }

    //region actions

    /**
     * Adds a melee unit to this node.
     */
    public void addMeleeUnit() {
        mMeleeUnits++;
    }

    /**
     * Adds a tank unit to this node.
     */
    public void addTankUnit() {
        mTankUnits++;
    }

    /**
     * Adds a sprinter unit to this node.
     */
    public void addSprinterUnit() {
        mSprinterUnits++;
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
        if (mHealthLevel >= Constants.NODE_HEALTH_MAX_LEVEL) {
            return;
        }
        mHealthLevel++;
    }

    /**
     * Upgrades the damage level to a max of 3.
     */
    public void upgradeDamage() {
        if (mDamageLevel >= Constants.NODE_DAMAGE_MAX_LEVEL) {
            return;
        }
        mDamageLevel++;
    }

    /**
     * Sets the health to full again.
     */
    public void repair() {
        mHealth = getMaxHealth();
        updateLabel();
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
            return;
        }
        if (unit instanceof TankUnit) {
            mTankUnits = 0;
            return;
        }
        if (unit instanceof SprinterUnit) {
            mSprinterUnits = 0;
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

            if (deductUnitHealth(attackDamage)) {
                return;
            }

            if (getState() instanceof OwnedState && ((OwnedState) getState()).getOwner().isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_LOST);
            }

            setState(new NeutralState(this));
            DeathParticleDecorator particles = new DeathParticleDecorator(mCircle, Constants.DEATH_PARTICLE_LAYER);
            particles.register();

            mHealth = 0;
        } else {
            mHealth = newHealth;
        }

        updateLabel();
    }

    /**
     * Deducts the attack damage from the defending unit. If the units absorbed the
     * damaged <code>true</code> is returned.
     *
     * @param attackDamage amount of damage
     * @return true if damage was absorbed, false otherwise
     */
    private boolean deductUnitHealth(int attackDamage) {
        int newHealth;
        if (mMeleeUnits > 0) {
            newHealth = mMeleeHealth - attackDamage;
            if (newHealth <= 0) {
                mMeleeUnits--;
                mMeleeHealth = Constants.UNIT_MELEE_HEALTH;
                return true;
            }
            mMeleeHealth = newHealth;
            return true;
        }

        if (mTankUnits > 0) {
            newHealth = mTankHealth - attackDamage;
            if (newHealth <= 0) {
                mTankUnits--;
                mTankHealth = Constants.UNIT_TANK_HEALTH;
                return true;
            }
            mTankHealth = newHealth;
            return true;
        }

        if (mSprinterUnits > 0) {
            newHealth = mSprinterHealth - attackDamage;
            if (newHealth <= 0) {
                mSprinterUnits--;
                mSprinterHealth = Constants.UNIT_SPRINTER_HEALTH;
                return true;
            }
            mSprinterUnits = newHealth;
            return true;
        }

        return false;
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

        updateLabel();
    }

    /**
     * Updates the health label of this node.
     */
    private void updateLabel() {
        mHealthLabel.setText(String.valueOf(mHealth) + Text.HEALTH);
    }

    @Override
    public void onClick() {
        if (mSelectingNode) {
            if (this.equals(mSourceNode)
                    || !Game.getInstance().getConnectedNodes(mSourceNode).contains(this)) {
                showNodeMenu();
                return;
            }

            switch (mSendingUnitType) {
                case MELEE:
                    mSourceNode.sendMeleeUnits(this);
                    break;
                case SPRINTER:
                    mSourceNode.sendSprinterUnits(this);
                    break;
                case TANK:
                    mSourceNode.sendTankUnits(this);
                    break;
            }
            SoundEngine.getInstance().play(SoundEngine.Sounds.UNIT_SENT);
        } else {
            showNodeMenu();
        }

    }

    @Override
    public void onUnhandledClick() {
        clearCoronas();
        if (mNodeMenu != null && mNodeMenu.isVisible()) {
            mNodeMenu.hide();
        }
    }

    @Override
    public float getWidth() {
        return Constants.NODE_RADIUS * 2;
    }

    @Override
    public float getHeight() {
        return Constants.NODE_RADIUS * 2;
    }

    /**
     *
     */
    private void showNodeMenu() {
        if (mNodeMenu != null && mNodeMenu.isVisible()) {
            mNodeMenu.hide();
            return;
        }

        if (getState() instanceof NeutralState || !(getState() instanceof OwnedState)) {
            return;
        }

        OwnedState state = (OwnedState) getState();

        mNodeMenu = new NodeMenu(this, state.getOwner().isHuman());
        mNodeMenu.show();
    }

    /**
     * Asks the player the player for the units to be sent to.
     *
     * @param type type of unit
     */
    public void askPlayerForTargetNode(EUnitType type) {
        mSelectingNode = true;
        mSourceNode = this;
        mSendingUnitType = type;
        mCoronas.clear();

        for (Node neighbor : Game.getInstance().getConnectedNodes(this)) {
            Polygon corona = new Polygon(neighbor.getPosition(), Colors.CORONA, 1, 300, 0, .75f);
            corona.register();
            mCoronas.add(corona);
        }
    }

    /**
     * Clears coronas off nodes.
     */
    private void clearCoronas() {
        for (Shape corona : mCoronas) {
            corona.destroy();
        }
    }

    //endregion

    //region getters/setters

    /**
     * @return gets the cost to repair the node with the current health
     */
    public int getRepairCost() {
        return (getMaxHealth() - getHealth()) * Constants.NODE_REPAIR_COST_MULTIPLIER;
    }

    /**
     * @return gets the current health
     */
    public int getHealth() {
        return mHealth;
    }

    /**
     * @return gets the current maximum of health (depends on health level)
     */
    public int getMaxHealth() {
        switch (mHealthLevel) {
            case 1:
                return Constants.NODE_MAX_HEALTH_1;
            case 2:
                return Constants.NODE_MAX_HEALTH_2;
            case 3:
                return Constants.NODE_MAX_HEALTH_3;
            default:
                throw new IllegalStateException("Level must be 1, 2 or 3");
        }
    }

    /**
     * @return gets melee unit count
     */
    public int getMeleeCount() {
        return mMeleeUnits;
    }

    /**
     * @return gets count of tank units
     */
    public int getTankCount() {
        return mTankUnits;
    }

    /**
     * @return gets count of sprinter units
     */
    public int getSprinterCount() {
        return mTankUnits;
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

        // notify AI that a new node was conquered
        AIAwareness.update();
    }

    /**
     * @return gets if the maximum health level has been reached
     */
    public boolean maxHealthLevelReached() {
        return mHealthLevel >= Constants.NODE_HEALTH_MAX_LEVEL;
    }

    /**
     * @return gets if the maximum damage level has been reached
     */
    public boolean maxDamageLevelReached() {
        return mDamageLevel >= Constants.NODE_DAMAGE_MAX_LEVEL;
    }

    /**
     * @return gets the amount of damage the node inflicts to intruders
     */
    public int getDamage() {
        // if health is zero, the units are defending until their last breath
        if (mHealth == 0) {
            if (mMeleeUnits > 0) {
                return Constants.UNIT_MELEE_ATTACK_DAMAGE;
            }
            if (mTankUnits > 0) {
                return Constants.UNIT_TANK_ATTACK_DAMAGE;
            }
            if (mSprinterUnits > 0) {
                return Constants.UNIT_SPRINTER_ATTACK_DAMAGE;
            }
            return 0;
        }

        switch (mDamageLevel) {
            case 1:
                return Constants.NODE_DAMAGE_1;
            case 2:
                return Constants.NODE_DAMAGE_2;
            case 3:
                return Constants.NODE_DAMAGE_3;
            default:
                throw new IllegalArgumentException("Damage level " + mDamageLevel + " is not allowed");
        }
    }

    //endregion

}
