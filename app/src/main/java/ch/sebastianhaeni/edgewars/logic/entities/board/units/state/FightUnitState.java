package ch.sebastianhaeni.edgewars.logic.entities.board.units.state;

import java.util.Random;

import ch.sebastianhaeni.edgewars.graphics.drawables.decorators.TextDecorator;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.Unit;

/**
 * The state of a unit fighting another unit at an opponent node.
 */
public class FightUnitState extends OnEdgeState {
    private final Unit mFightingUnit;
    private final Random mRandom = new Random();
    private final Polygon mFightingUnitShape;
    private final TextDecorator mFightingUnitText;

    /**
     * Constructor
     *
     * @param unit              the unit having this state
     * @param node              the node the fight happens
     * @param player            the player this unit belongs to
     * @param edge              the edge this unit is on
     * @param travelledDistance travelled distance on edge
     * @param fightingUnit      the opponent unit to be fought by this unit
     */
    public FightUnitState(Unit unit, Node node, Player player, Edge edge, float travelledDistance, Unit fightingUnit) {
        super(unit, node, player, edge, travelledDistance);
        mFightingUnit = fightingUnit;
        mFightingUnitShape = ((OnEdgeState) mFightingUnit.getState()).getShape();
        mFightingUnitText = ((OnEdgeState) mFightingUnit.getState()).getText();
    }

    @Override
    public void update(long millis) {
        if (getUnit().getState() instanceof DeadState) {
            invalidate();
            return;
        }

        if (mFightingUnit.getState() instanceof DeadState) {
            invalidate();
            getUnit().setState(new MovingState(getUnit(), getNode(), getPlayer(), getEdge(), getTravelledDistance()));
            return;
        }

        if (mRandom.nextFloat() < getUnit().getAccuracy()) {
            mFightingUnit.deductHealth(getUnit().getAttackDamage());
        }

        if (mRandom.nextFloat() < mFightingUnit.getAccuracy()) {
            getUnit().deductHealth(mFightingUnit.getAttackDamage());
        }

        if (mFightingUnit.getState() instanceof DeadState) {
            mFightingUnitShape.destroy();
            mFightingUnitText.destroy();
        } else {
            mFightingUnitText.setText(String.valueOf(mFightingUnit.getCount()));
            mFightingUnitText.calculateVertexBuffer();
        }

        if (getUnit().getState() instanceof DeadState) {
            invalidate();
        } else {
            getText().setText(String.valueOf(getUnit().getCount()));
            getText().calculateVertexBuffer();
        }
    }

    @Override
    public long getUpdateInterval() {
        return Constants.FIGHT_UNIT_STATE_UPDATE_INTERVAL;
    }
}
