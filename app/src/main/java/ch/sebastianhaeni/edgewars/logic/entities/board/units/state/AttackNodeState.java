package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import android.util.Log;

import java.util.Random;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * State of a unit attacking a node.
 */
public class AttackNodeState extends UnitState {
    public static final String TAG = "AttackNodeState";
    private final Node mNode;
    private final Random mRandom = new Random();

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
        Log.d(TAG, "Attacking with " + getUnit());
        if (mRandom.nextFloat() < getUnit().getAccuracy()) {
            mNode.deductHealth(getUnit().getAttackDamage());
            getUnit().deductHealth(mNode.getDamage());
        } else {
            Log.d(TAG, "missed!");
        }
    }

    @Override
    public long getUpdateInterval() {
        return 1000;
    }
}
