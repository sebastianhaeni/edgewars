package ch.sebastianhaeni.edgewars.logic.entities.board;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Line;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * An edge connects two nodes and has no functionality.
 */
public class Edge extends BoardEntity {
    private final Node mSourceNode;
    private final Node mTargetNode;
    private final ArrayList<Drawable> mDrawables = new ArrayList<>();

    /**
     * Constructor
     *
     * @param sourceNode start edge
     * @param targetNode target edge
     */
    public Edge(Node sourceNode, Node targetNode) {
        super();
        mSourceNode = sourceNode;
        mTargetNode = targetNode;

        mDrawables.clear();
        mDrawables.add(new Line(sourceNode.getPosition(), targetNode.getPosition(), Colors.EDGE));
    }

    @Override
    public void update(long millis) {
        // no op
    }

    /**
     * @return gets the source node
     */
    public Node getSourceNode() {
        return mSourceNode;
    }

    /**
     * @return gets the target node
     */
    public Node getTargetNode() {
        return mTargetNode;
    }
}
