package ch.bfh.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.ai.rules.Rule;
import ch.bfh.edgewars.logic.commands.Command;

public class ExposedNodeRule extends Rule {
    public ExposedNodeRule(GameState state) {
        super(state);
    }

    @Override
    public boolean applies() {
        return false;
    }

    @Override
    public ArrayList<Command> getCommands() {
        return null;
    }
}
