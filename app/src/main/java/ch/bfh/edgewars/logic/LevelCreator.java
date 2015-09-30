package ch.bfh.edgewars.logic;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.ai.RuleBasedAI;
import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.node.state.OwnedState;
import ch.bfh.edgewars.util.Position;

public class LevelCreator {
    public GameState build() {
        Camera camera = new Camera();
        Board board = new Board();

        ArrayList<Player> players = new ArrayList<>();

        Player human = new Player();
        Player computer = new Player(new RuleBasedAI());

        Node node = new Node(new Position(0, 0));
        node.setState(new OwnedState(node, human));

        board.addEntity(node);

        players.add(human);
        players.add(computer);

        return new GameState(camera, board, players);
    }
}
