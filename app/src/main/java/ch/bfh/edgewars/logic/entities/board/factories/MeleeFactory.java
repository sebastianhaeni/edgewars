package ch.bfh.edgewars.logic.entities.board.factories;

import ch.bfh.edgewars.logic.entities.board.Node;
import ch.bfh.edgewars.logic.entities.board.units.MeleeUnit;

public class MeleeFactory extends Factory {
    public MeleeFactory(Node node) {
        super(node);
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
    protected void produceUnit() {
        getNode().addUnit(new MeleeUnit());
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
                return 700;
            default:
                return -1;
        }
    }

}
