package ch.sebastianhaeni.edgewars.logic.entities.board.units;

import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.DeathParticleDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.BoardEntity;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.DeadState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.IdleState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.MovingState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.state.UnitState;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * A unit owned by a player and produced at a node from a factory.
 */
public abstract class Unit extends BoardEntity {

    private final Node mNode;
    private final Player mPlayer;
    private UnitState mState;
    private int mCount;
    private int mHealth;
    private Polygon mShape;
    private TextDecorator mText;
    private boolean mIsShowing;

    /**
     * Constructor
     *
     * @param count  count of units in this container
     * @param node   the node this unit goes to
     * @param player the owning player
     */
    Unit(int count, Node node, Player player) {
        super(-1); // we don't know the interval yet
        setUpdateInterval(Constants.UNIT_UPDATE_INTERVAL); // now we know it

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

    /**
     * Sends the unit along the edge to the target node.
     *
     * @param node to which node the unit should move
     * @param edge the edge the unit moves on
     */
    public void move(Node node, Edge edge) {
        setState(new MovingState(this, node, getPlayer(), edge, 0));
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
            updateCount();
            if (mCount <= 0) {
                setState(new DeadState(this));
                DeathParticleDecorator particles = new DeathParticleDecorator(mShape, Constants.DEATH_PARTICLE_LAYER);
                particles.register();
            }
            mHealth = getMaxHealth();
        } else {
            mHealth = newHealth;
        }
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

    /**
     * Creates the shape of this unit and registers it.
     *
     * @param position position of the shape
     * @param color    color of the unit
     */
    public void show(Position position, float[] color) {
        if (mIsShowing) {
            return;
        }
        mIsShowing = true;
        mShape = new Polygon(
                position,
                color,
                Constants.UNIT_SHAPE_LAYER,
                getPolygonCorners(),
                0,
                Constants.UNIT_RADIUS);

        mText = new TextDecorator(mShape, String.valueOf(mCount), Constants.UNIT_TEXT_LAYER);

        mShape.register();
        mText.register();
    }

    /**
     * @return gets the shape of this unit or <code>null</code> if this unit is not rendered
     */
    public Polygon getShape() {
        return mShape;
    }

    /**
     * @return gets text decorator
     */
    public TextDecorator getText() {
        return mText;
    }

    public void updateCount() {
        mText.setText(String.valueOf(mCount));
        mText.calculateVertexBuffer();
    }

    /**
     * Hides the unit and removes all rendering shapes.
     */
    public void hide() {
        if (mShape != null) {
            mShape.destroy();
        }
        if (mText != null) {
            mText.destroy();
        }
        mIsShowing = false;
    }

    /**
     * Updates the position of the unit and forces the render components to update.
     *
     * @param position the new position
     */
    public void updatePosition(Position position) {
        mShape.getPosition().set(position);
        mShape.calculateVertexBuffer();
        mText.calculateVertexBuffer();
    }
}
