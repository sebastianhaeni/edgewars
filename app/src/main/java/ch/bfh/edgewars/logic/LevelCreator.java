package ch.bfh.edgewars.logic;

import android.content.Context;

import java.util.ArrayList;

import ch.bfh.edgewars.logic.ai.RuleBasedAI;
import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.node.state.OwnedState;
import ch.bfh.edgewars.logic.levels.LevelLoader;
import ch.bfh.edgewars.util.Colors;
import ch.bfh.edgewars.util.Position;

public class LevelCreator {

    private Context mContext;

    public LevelCreator (Context context) {
        mContext = context;
    }

    public GameState build() {
        //testing
        LevelLoader loader = new LevelLoader(mContext);
        Board board = loader.createBoard(1);

        Camera camera = new Camera();

        /*
        Board board = new Board();

        ArrayList<Player> players = new ArrayList<>();

        Player human = new Player(Colors.BLUE);
        Player computer = new Player(Colors.RED);

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

        GameState state = new GameState(camera, board, human);
        */

        GameState state = new GameState(camera, board, new Player(Colors.BLUE));

        //computer.setAI(new RuleBasedAI(state));

        return state;
    }
}
