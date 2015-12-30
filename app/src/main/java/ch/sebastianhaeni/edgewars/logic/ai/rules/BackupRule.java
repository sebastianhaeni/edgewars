package ch.sebastianhaeni.edgewars.logic.ai.rules;


import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.ai.AIAwareness;
import ch.sebastianhaeni.edgewars.logic.commands.Command;
import ch.sebastianhaeni.edgewars.logic.commands.MoveUnitCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class BackupRule extends Rule {

    private long mTimePassed;
    private Node mNode;
    private Node mBackupTarget;

    public BackupRule(Player player) {
        super(player);
    }

    @Override
    public boolean applies(Node node, long millis) {
        mTimePassed += millis;
        if (mTimePassed < 4000) {
            return false;
        }
        mTimePassed = 0;
        mNode = node;

        mBackupTarget = AIAwareness.getBackupTargetNode(mNode);

        return AIAwareness.getDistanceToEnemy(mNode) >= 2 && mBackupTarget != null && (mNode.getTankCount() >= 5 || mNode.getSprinterCount() >= 5 || mNode.getMeleeCount() >= 5);
    }

    @Override
    public ArrayList<Command> getCommands() {

        ArrayList<Command> commands = new ArrayList<>();

        int sprinterCount = mNode.getSprinterCount();
        int meleeCount = mNode.getMeleeCount();
        int tankCount = mNode.getTankCount();

        if (sprinterCount >= meleeCount && sprinterCount >= tankCount) {
            commands.add(new MoveUnitCommand(mNode.getSprinterCount(), EUnitType.SPRINTER, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        } else if (meleeCount >= sprinterCount && meleeCount >= tankCount) {
            commands.add(new MoveUnitCommand(mNode.getMeleeCount(), EUnitType.MELEE, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        } else {
            commands.add(new MoveUnitCommand(mNode.getTankCount(), EUnitType.TANK, mBackupTarget, Game.getInstance().getEdgeBetween(mNode, mBackupTarget), getPlayer()));
        }

        return commands;
    }
}
