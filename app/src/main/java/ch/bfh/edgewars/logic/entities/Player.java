package ch.bfh.edgewars.logic.entities;

public class Player extends Entity {
    private int mEnergy;

    public Player() {
        super(1000);
    }

    @Override
    public void update(long millis) {
        // TODO increase energy based on owned node count
        // TODO add energy from freshly captured neutral nodes
    }

    public int getEnergy() {
        return mEnergy;
    }
}
