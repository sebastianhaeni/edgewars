package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.commands.ActivateFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

class BuildUpRule extends Rule {
    private long mTimePassed;
    private boolean isInitialized;
    private EUnitType currentProduction;
    private ArrayList<Command> commands;

    public BuildUpRule(Player player) {
        super(player);
        isInitialized = false;
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.BUILDUP_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;

        commands = new ArrayList<>();

        if (!isInitialized) {
            currentProduction = EUnitType.TANK;
            commands.add(new ActivateFactoryCommand(node.getTankFactory()));
            isInitialized = true;
            return true;
        }

        switch (currentProduction) {
            case TANK:
                if (node.getTankCount() >= Constants.MIN_TANK_ATTACK_COUNT) {
                    commands.add(new ActivateFactoryCommand(node.getMeleeFactory()));
                    currentProduction = EUnitType.MELEE;
                }
                break;
            case MELEE:
                if (node.getMeleeCount() >= Constants.MIN_MELEE_ATTACK_COUNT) {
                    commands.add(new ActivateFactoryCommand(node.getSprinterFactory()));
                    currentProduction = EUnitType.SPRINTER;
                }
                break;
            case SPRINTER:
                if (node.getSprinterCount() >= Constants.MIN_SPRINTER_ATTACK_COUNT) {
                    commands.add(new ActivateFactoryCommand(node.getTankFactory()));
                    currentProduction = EUnitType.TANK;
                }
                break;
        }

        return !commands.isEmpty();
    }

    @Override
    public ArrayList<Command> getCommands() {
        return commands;
    }
}
