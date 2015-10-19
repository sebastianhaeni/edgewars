package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.graphics.shapes.Shape;
import ch.sebastianhaeni.edgewars.graphics.shapes.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.DeadState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.IdleState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.MovingState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.UnitState;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A unit owned by a player and produced at a node from a factory.
 */
public abstract class Unit extends BoardEntity {

    private final int mCount;
    private final Position mPosition;
    private final Node mNode;
    private UnitState mState;
    private int mHealth;

    /**
     * Constructor
     *
     * @param count count of units in this container
     * @param node  the node this unit starts at
     */
    public Unit(int count, Node node) {
        super(-1);
        setUpdateInterval(getSpeed());
        setState(new IdleState(this));
        mHealth = getMaxHealth();
        mCount = count;
        mNode=node;
        mPosition = new Position(node.getPosition().getX(), node.getPosition().getY());
        getDrawables().add(new TextDecorator(getShape(), String.valueOf(mCount)));
    }

    /**
     * @return gets the shape that represents this unit
     */
    protected abstract Shape getShape();

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
     * @return gets position
     */
    public Position getPosition() {
        return mPosition;
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
        getState().update(millis);
    }

    /**
     * Sends the unit along the edge to the target node.
     *
     * @param node to which node the unit should move
     * @param edge the edge the unit moves on
     */
    public void move(Node node, Edge edge) {
        setState(new MovingState(this, node, ((OwnedState) edge.getSourceNode().getState()).getOwner(), edge));
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
            setState(new DeadState(this));
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
}
