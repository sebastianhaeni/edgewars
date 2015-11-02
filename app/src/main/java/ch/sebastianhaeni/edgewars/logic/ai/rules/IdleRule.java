package ch.sebastianhaeni.edgewars.logic.ai.rules;

import android.util.Log;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.BuildMeleeUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;

public class IdleRule extends Rule {
    private Node mNode;
    private Node mTarget;
    private long mTimePassed;

    public IdleRule(GameState state, Player player) {
        super(state, player);
    }

    @Override
    public boolean applies(long millis) {
        mTimePassed += millis;
        if (mTimePassed < 10000) {
            return false;
        }
        mTimePassed = 0;

        mNode = null;
        mTarget = null;

        for (Node n : getState().getBoard().getNodes()) {
            if (n.getState() instanceof OwnedState) {
                OwnedState state = (OwnedState) n.getState();

                Player owner = state.getOwner();
                Player player = getPlayer();

                if (owner.equals(player)) {
                    mNode = n;
                    break;
                }
            }
        }

        if (mNode == null) {
            return false;
        }

        for (Node t : Game.getInstance().getConnectedNodes(mNode)) {
            if (t.getState() instanceof NeutralState) {
                mTarget = t;
                break;
            } else if (t.getState() instanceof OwnedState
                    && !((OwnedState) t.getState()).getOwner().equals(getPlayer())) {
                mTarget = t;
                break;
            }
        }

        return mNode != null && mTarget != null;
    }

    @Override
    public ArrayList<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>();

        Log.d("IdleRule", "Source: " + mNode.getPosition());
        Log.d("IdleRule", "Target: " + mTarget.getPosition());

        if (mNode.getMeleeCount() >= 10) {
            commands.add(new MoveUnitCommand(
                    new MeleeUnit(
                            mNode.getMeleeCount(),
                            mTarget, getPlayer()),
                    mTarget,
                    Game.getInstance().getEdgeBetween(mNode, mTarget)));
        }

        commands.add(new BuildMeleeUnitCommand(mNode.getMeleeFactory()));

        return commands;
    }
}
