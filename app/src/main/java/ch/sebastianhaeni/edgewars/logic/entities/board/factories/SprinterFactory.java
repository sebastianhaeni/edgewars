package ch.sebastianhaeni.edgewars.logic.entities.board.factories;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;

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
                return 30;
            case 2:
                return 40;
            case 3:
                return 60;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }

    @Override
    public int getUnitCost() {
        return 20;
    }

    @Override
    protected long getProducingDuration() {
        switch (getLevel()) {
            case 1:
                return 10000;
            case 2:
                return 7000;
            case 3:
                return 4000;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }
}
