package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import android.util.Log;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.DeadState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.IdleState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.MovingState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.UnitState;

/**
 * A unit owned by a player and produced at a node from a factory.
 */
public abstract class Unit extends BoardEntity {

    private final Node mNode;
    private final Player mPlayer;
    private UnitState mState;
    private int mCount;
    private int mHealth;

    /**
     * Constructor
     *
     * @param count  count of units in this container
     * @param node   the node this unit goes to
     * @param player the owning player
     */
    Unit(int count, Node node, Player player) {
        super(-1);
        setUpdateInterval(10);

        mHealth = getMaxHealth();
        mCount = count;
        mNode = node;
        mPlayer = player;

        // let the unit idle
        mState = new IdleState(this);
    }

    /**
     * @return gets the name of this unit type
     */
    public abstract String getName();

    /**
     * @return gets the attack damage of this unit
     */
    public abstract int getAttackDamage();

    /**
     * @return gets the max health of this unit
     */
    protected abstract int getMaxHealth();

    /**
     * @return gets the accuracy of this unit in percent (0.0 - 1.0)
     */
    public abstract float getAccuracy();

    /**
     * @return gets the time in which the unit goes a fixed distance
     */
    public abstract long getSpeed();

    /**
     * @return gets the unit's state
     */
    public UnitState getState() {
        return mState;
    }

    /**
     * Sets the unit's state.
     *
     * @param state the new state
     */
    public void setState(UnitState state) {
        mState = state;
        setUpdateInterval(state.getUpdateInterval());
    }

    @Override
    public void update(long millis) {
        if (mState == null) {
            return;
        }
        mState.update(millis);
    }

    @Override
    public void initialize() {
        // no op
    }

    /**
     * Sends the unit along the edge to the target node.
     *
     * @param node to which node the unit should move
     * @param edge the edge the unit moves on
     */
    public void move(Node node, Edge edge) {
        setState(new MovingState(this, node, getPlayer(), edge));
    }

    /**
     * Deducts the unit's health with a damage value. If the health goes <= 0 then the DeadState
     * is set.
     *
     * @param attackDamage amount of damage inflicted
     */
    public void deductHealth(int attackDamage) {
        int newHealth = mHealth - attackDamage;
        if (newHealth <= 0) {
            mCount--;
            if (mCount <= 0) {
                Log.d("Unit", "Unit died!");
                setState(new DeadState(this));
            }
            return;
        }
        mHealth = newHealth;
    }

    /**
     * @return gets the node
     */
    public Node getNode() {
        return mNode;
    }

    /**
     * @return gets unit count
     */
    public int getCount() {
        return mCount;
    }

    /**
     * @return gets the amount of corners the polygon representation for this unit has
     */
    public abstract int getPolygonCorners();

    /**
     * @return gets the owning player
     */
    public Player getPlayer() {
        return mPlayer;
    }
}
