package ch.sebastianhaeni.edgewars.logic;

import android.util.Log;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;
import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

public class LevelCreator {
    public GameState build() {
        Log.d("LevelCreator", "Generating level");

        Camera camera = new Camera();
        Board board = new Board();

        ArrayList<Player> players = new ArrayList<>();

        Player human = new Player(Colors.NODE_MINE);
        Player computer = new Player(Colors.NODE_OPPONENT);

        Node node1 = new Node(new Position(-5, 0));
        node1.setState(new OwnedState(node1, human));

        Node node2 = new Node(new Position(0, -3));

        Node node3 = new Node(new Position(5, 1));
        node3.setState(new OwnedState(node3, computer));

        // add edges before nodes so they are drawn under them
        board.addEntity(new Edge(node1, node2));
        board.addEntity(new Edge(node2, node3));

        board.addEntity(node1);
        board.addEntity(node2);
        board.addEntity(node3);

        players.add(human);
        players.add(computer);

        GameState state = new GameState(camera, board, players, human);

        computer.setAI(new RuleBasedAI(state));

        return state;
    }
}
