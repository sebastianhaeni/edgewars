package ch.bfh.edgewars.logic.entities.board.factories;

import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.units.SprinterUnit;

public class SprinterFactory extends Factory {
    public SprinterFactory(Node node) {
        super(node);
    }

    @Override
    public void produceUnit() {
        getNode().addUnit(new SprinterUnit(getNode()));
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
