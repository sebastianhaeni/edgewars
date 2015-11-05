package ch.sebastianhaeni.edgewars.logic.entities.board.factories;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * A concrete factory that builds melee units.
 */
public class SprinterFactory extends Factory {

    /**
     * Constructor
     *
     * @param node the node this factory is at
     */
    public SprinterFactory(Node node) {
        super(node);
    }

    @Override
    public void produceUnit() {
        getNode().addSprinterUnit();
        notifyPropertyChanged(BR.sprinterCount);
    }

    @Override
    public int getUpgradeCost() {
        switch (getLevel()) {
            case 1:
                return Constants.FACTORY_SPRINTER_UPGRADE_1;
            case 2:
                return Constants.FACTORY_SPRINTER_UPGRADE_2;
            case 3:
                return Constants.FACTORY_SPRINTER_UPGRADE_3;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }

    @Override
    public int getUnitCost() {
        return Constants.FACTORY_SPRINTER_UNIT_COST;
    }

    @Override
    protected long getProducingDuration() {
        switch (getLevel()) {
            case 1:
                return Constants.FACTORY_SPRINTER_PRODUCING_DURATION_1;
            case 2:
                return Constants.FACTORY_SPRINTER_PRODUCING_DURATION_2;
            case 3:
                return Constants.FACTORY_SPRINTER_PRODUCING_DURATION_3;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }
}
