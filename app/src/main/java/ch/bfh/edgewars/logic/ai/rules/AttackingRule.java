package ch.bfh.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.commands.Command;

public class AttackingRule extends Rule {
    public AttackingRule(GameState state) {
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