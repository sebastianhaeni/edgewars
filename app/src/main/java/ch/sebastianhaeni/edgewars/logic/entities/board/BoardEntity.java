package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;

/**
 * A board entity is an entity that's visible on the game board.
 */
public abstract class BoardEntity extends Entity {

    private final ArrayList<Drawable> mDrawables = new ArrayList<>();

    /**
     * Constructor for updating entities.
     *
     * @param interval the interval time this entity wants to be updated
     */
    public BoardEntity(long interval) {
        super(interval);
    }

    /**
     * Constructor for entities that don't want to be updated.
     */
    public BoardEntity() {
        super();
    }

}
