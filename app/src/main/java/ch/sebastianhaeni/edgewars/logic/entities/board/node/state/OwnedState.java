package ch.sebastianhaeni.edgewars.logic.entities.board.node.state;

import android.util.Log;

import java.util.Arrays;

import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.BuildUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * The state a node is in when it is owned by a player.
 */
public class OwnedState extends NodeState {
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
        mOwner.addEnergy(Constants.OWNED_STATE_ENERGY_GAIN);
        getNode().addHealth(Constants.OWNED_STATE_HEALTH_GAIN);

        buildUnits();
    }

    /**
     * Building units automatically. This method adds 12 new build commands when the remaining
     * producing stack has fallen under 2.
     */
    private void buildUnits() {
        if (getOwner().getColor() == Colors.NODE_MINE) {
            Log.d("Node", "Melee:   " + getNode().getMeleeCount());
            Log.d("Node", "Tank:    " + getNode().getTankCount());
            Log.d("Node", "Sprinter:" + getNode().getSprinterCount());
            Log.d("Node", "\n");
        }

        // issue build command
        Factory factory = null;
        if (getNode().getBuildUnitType() == MeleeUnit.class) {
            factory = getNode().getMeleeFactory();
        } else if (getNode().getBuildUnitType() == TankUnit.class) {
            factory = getNode().getTankFactory();
        } else if (getNode().getBuildUnitType() == SprinterUnit.class) {
            factory = getNode().getSprinterFactory();
        }

        if (factory != null) {
            if (factory.getProducingStack() > 2) {
                return;
            }
            Command command = new BuildUnitCommand(factory, 12);
            Game.getInstance().register(command);
        }

    }

    @Override
    public long getUpdateInterval() {
        return Constants.OWNED_STATE_UPDATE_INTERVAL;
    }

    @Override
    public String toString() {
        return Arrays.toString(mOwner.getColor());
    }

    public Player getOwner() {
        return mOwner;
    }
}
