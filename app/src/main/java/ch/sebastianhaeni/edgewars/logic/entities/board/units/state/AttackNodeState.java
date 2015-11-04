package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * State of a unit attacking a node.
 */
public class AttackNodeState extends UnitState {
    private final Node mNode;
    private final Random mRandom = new Random();
    private boolean mIsStateInvalid;

    /**
     * Constructor
     *
     * @param unit the unit having this state
     * @param node the node attacked
     */
    public AttackNodeState(Unit unit, Node node) {
        super(unit);
        mNode = node;
    }

    @Override
    public void update(long millis) {
        if (mIsStateInvalid) {
            return;
        }
        if (mNode.getState() instanceof NeutralState) {
            mNode.setState(new OwnedState(mNode, getUnit().getPlayer()));
            mNode.addUnit(getUnit());
            if (getUnit().getPlayer().isHuman()) {
                SoundEngine.getInstance().play(SoundEngine.Sounds.NODE_CAPTURED);
            }
            mIsStateInvalid = true;
            return;
        }

        getUnit().deductHealth(mNode.getDamage());

        if (mRandom.nextFloat() < getUnit().getAccuracy()) {
            mNode.deductHealth(getUnit().getAttackDamage());
        }
    }

    @Override
    public long getUpdateInterval() {
        return ATTACK_NODE_UPDATE_INTERVAL;
    }
}
