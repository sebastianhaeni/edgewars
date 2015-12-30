package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public abstract class Rule {
    private final Player mPlayer;

    Rule(Player player) {
        mPlayer = player;
    }

    /**
     * Checks whether the rule applies to a given node of a player
     *
     * @param node   the player's node to be assessed
     * @param millis current play time in ms
     * @return true if the rule applies to the node, false if not
     */
    public abstract boolean applies(Node node, long millis);

    /**
     * Gets the commands associated with this rule. Since this method is generally specific
     * to a given node, this method should only be called after calling applies()-method
     *
     * @return list of commands associated with this rule
     */
    public abstract ArrayList<Command> getCommands();

    Player getPlayer() {
        return mPlayer;
    }
}
