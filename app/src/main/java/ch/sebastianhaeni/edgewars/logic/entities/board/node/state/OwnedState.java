package ch.sebastianhaeni.edgewars.logic.entities.board.node.state;

import java.util.Arrays;

import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * The state a node is in when it is owned by a player.
 */
public class OwnedState extends NodeState {
    private static final int ENERGY_GAIN = 2;
    private static final int HEALTH_GAIN = 1;
    private final Player mOwner;

    /**
     * Constructor
     *
     * @param node  the node that has this state
     * @param owner the player that owns the node
     */
    public OwnedState(Node node, Player owner) {
        super(node);
        mOwner = owner;
        node.clearUnitsAndLevels();
        node.setColor(owner.getColor());
    }

    @Override
    public void update(long millis) {
        mOwner.addEnergy(ENERGY_GAIN);
        getNode().addHealth(HEALTH_GAIN);
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }

    @Override
    public String toString() {
        return Arrays.toString(mOwner.getColor());
    }

    public Player getOwner() {
        return mOwner;
    }
}
