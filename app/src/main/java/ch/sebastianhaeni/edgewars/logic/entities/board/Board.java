package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.shapes.IDrawable;
import ch.sebastianhaeni.edgewars.logic.entities.Entity;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 * The board is the controller of everything visible on the game board. It has board entities and
 * drawables.
 */
public class Board extends Entity {
    private final ArrayList<BoardEntity> mEntities = new ArrayList<>();

    /**
     * Constructor
     */
    public Board() {
        super();
    }

    /**
     * Adds a board entity to the board.
     *
     * @param e new board entity
     */
    public void addEntity(BoardEntity e) {
        mEntities.add(e);
    }



    /**
     * @return gets the nodes on the board
     */
    public ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        for (BoardEntity e : mEntities) {
            if (e instanceof Node) {
                nodes.add((Node) e);
            }
        }
        return nodes;
    }

    @Override
    public void update(long millis) {
        // no op
    }
}
