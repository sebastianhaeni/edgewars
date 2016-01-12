package ch.sebastianhaeni.edgewars.logic.ai.rules;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

class BackupRule extends Rule {

    private long mTimePassed;
    private Node mNode;
    private Node mBackupTarget;

    private int minTankCount;
    private int minMeleeCount;
    private int minSprinterCount;

    private final int minDistanceToEnemy = 2;

    public BackupRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < Constants.BACKUP_RULE_UPDATE_INTERVAL) {
            return false;
        }
        mTimePassed = 0;
        mNode = node;

        int distanceToEnemy = AIAwareness.getDistanceToEnemy(mNode);
        mBackupTarget = AIAwareness.getBackupTargetNode(mNode);

        if (distanceToEnemy < minDistanceToEnemy || mBackupTarget == null) {
            return false;
        }

        if (distanceToEnemy > minDistanceToEnemy) {
            minTankCount = Constants.MIN_TANK_ATTACK_COUNT;
            minMeleeCount = Constants.MIN_MELEE_ATTACK_COUNT;
            minSprinterCount = Constants.MIN_SPRINTER_ATTACK_COUNT;
        } else if (distanceToEnemy == minDistanceToEnemy) {
            minTankCount = Constants.MIN_TANK_ATTACK_COUNT / 2;
            minMeleeCount = Constants.MIN_MELEE_ATTACK_COUNT / 2;
            minSprinterCount = Constants.MIN_SPRINTER_ATTACK_COUNT / 2;
        }

        return mNode.getTankCount() >= minTankCount || mNode.getSprinterCount() >= minSprinterCount || mNode.getMeleeCount() >= minMeleeCount;
    }

    @Override
    public ArrayList<Command> getCommands() {

        ArrayList<Command> commands = new ArrayList<>();

        if (mNode.getTankCount() >= minTankCount) {
            commands.add(new MoveUnitCommand(mNode.getTankCount(), EUnitType.TANK, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        } else if (mNode.getMeleeCount() >= minMeleeCount) {
            commands.add(new MoveUnitCommand(mNode.getMeleeCount(), EUnitType.MELEE, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        } else {
            commands.add(new MoveUnitCommand(mNode.getSprinterCount(), EUnitType.SPRINTER, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        }

        return commands;
    }
}
