package ch.sebastianhaeni.edgewars.logic.entities.board;

import ch.sebastianhaeni.edgewars.logic.entities.Entity;

/**
 * A board entity is an entity that's visible on the game board.
 */
public abstract class BoardEntity extends Entity {

    /**
     * Constructor for updating entities.
     *
     * @param interval the interval time this entity wants to be updated
     */
    protected BoardEntity(long interval) {
        super(interval);
    }

    /**
     * Constructor for entities that don't want to be updated.
     */
    protected BoardEntity() {
        super();
    }

}
