package ch.sebastianhaeni.edgewars.logic.entities.board.factories;

import ch.sebastianhaeni.edgewars.BR;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;

/**
 * A concrete factory that builds melee units.
 */
public class MeleeFactory extends Factory {

    /**
     * Constructor
     *
     * @param node the node this factory is at
     */
    public MeleeFactory(Node node) {
        super(node);
    }

    @Override
    protected void produceUnit() {
        getNode().addUnit(new MeleeUnit(getNode()));
        notifyPropertyChanged(BR.meleeCount);
    }

    @Override
    public int getUpgradeCost() {
        switch (getLevel()) {
            case 1:
                return 50;
            case 2:
                return 60;
            case 3:
                return 80;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }

    @Override
    public int getUnitCost() {
        return 10;
    }

    @Override
    protected long getProducingDuration() {
        switch (getLevel()) {
            case 1:
                return 2000;
            case 2:
                return 1000;
            case 3:
                return 400;
            default:
                throw new IllegalStateException("Level is not 1, 2 or 3");
        }
    }

}
