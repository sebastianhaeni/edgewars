package ch.sebastianhaeni.edgewars.logic.entities.board.node.state;

import ch.sebastianhaeni.edgewars.graphics.shapes.decorators.DeathParticleDecorator;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.util.Colors;

public class NeutralState extends NodeState {
    public NeutralState(Node node) {
        super(node);
        node.setColor(Colors.NEUTRAL);
    }

    @Override
    public void update(long millis) {
        // no op
    }

    @Override
    public long getUpdateInterval() {
        return -1;
    }
}
