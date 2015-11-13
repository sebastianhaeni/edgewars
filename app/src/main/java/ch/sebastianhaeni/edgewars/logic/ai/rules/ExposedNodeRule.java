package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class ExposedNodeRule extends Rule {
    public ExposedNodeRule(GameState state, Player player) {
        super(state, player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        return false;
    }

    @Override
    public ArrayList<Command> getCommands() {
        return null;
    }
}
