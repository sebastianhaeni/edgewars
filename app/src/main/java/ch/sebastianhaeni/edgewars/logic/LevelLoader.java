package ch.sebastianhaeni.edgewars.logic;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;
import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.levels.Level;
import ch.sebastianhaeni.edgewars.logic.levels.LevelDeserializer;
import ch.sebastianhaeni.edgewars.logic.levels.Levels;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

public class LevelLoader {

    public final ArrayList<Player> mComputerPlayers = new ArrayList<>();

    private final Context mContext;
    private Levels mLevels;

    public Player addComputerPlayer() {
        Player newComputerPlayer = new Player(Colors.NODE_OPPONENT);
        mComputerPlayers.add(newComputerPlayer);
        return newComputerPlayer;
    }

    public LevelLoader(Context context) {

        mContext = context;

        // TODO: validate levels.json with json schema
        // this.validateJsonFile();

        // load levels from json file to mLevels
       // this.loadLevelsFromJsonFile();

    }

    public GameState build(int levelNumber) {
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
      /*  Board board = new Board();
        Camera camera = new Camera();

        // TODO: do it better?
        Level level = mLevels.getLevels().get(levelNumber - 1);

        ArrayList<Node> nodes = level.getNodes();
        ArrayList<Edge> edges = level.getEdges();

        // first add edges to board, so that they are drawn under nodes
        for (Edge edge : edges) {
            board.addEntity(edge);
        }

        // then add the nodes
        for (Node node : nodes) {
            board.addEntity(node);
        }

        ArrayList<Player> players = new ArrayList<>(mComputerPlayers);

        Player humanPlayer = new Player(Colors.NODE_MINE);

        players.add(humanPlayer);
        GameState state = new GameState(camera, board, players, humanPlayer);

        // add AI to computer players
        for (Player computerPlayer : mComputerPlayers) {
            computerPlayer.setAI(new RuleBasedAI(state));
        }

        return state;*/
    }

    private void loadLevelsFromJsonFile() {
        // initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Levels.class, new LevelDeserializer());
        Gson gson = gsonBuilder.create();

        InputStream is = mContext.getApplicationContext().getResources().openRawResource(R.raw.levels);
        Reader r = new BufferedReader(new InputStreamReader(is));

        mLevels = gson.fromJson(r, Levels.class);
    }

}
