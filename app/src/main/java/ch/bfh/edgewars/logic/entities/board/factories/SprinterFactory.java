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
        // TODO adjust values
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
        // TODO adjust values
        return 10;
    }

    @Override
    protected long getProducingDuration() {
        // TODO adjust values
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
