package ch.sebastianhaeni.edgewars.logic;

import android.content.Context;
import android.util.Log;

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
import ch.sebastianhaeni.edgewars.logic.levels.Level;
import ch.sebastianhaeni.edgewars.logic.levels.LevelDeserializer;
import ch.sebastianhaeni.edgewars.logic.levels.Levels;
import ch.sebastianhaeni.edgewars.util.Colors;

public class LevelLoader {

    public static Player humanPlayer = new Player(Colors.NODE_MINE);
    public static ArrayList<Player> computerPlayers = new ArrayList<>();

    private final int LEVELS_RESOURCE_FILE = R.raw.levels;
    private final int SCHEMA_RESOURCE_FILE = R.raw.levels_schema;

    private Context mContext;
    private Levels mLevels;

    public static Player addComputerPlayer() {
        Player newComputerPlayer = new Player(Colors.NODE_OPPONENT);
        computerPlayers.add(newComputerPlayer);
        return newComputerPlayer;
    }

    public LevelLoader(Context context) {

        mContext = context;

        // TODO: validate levels.json with json schema
        // this.validateJsonFile();

        // load levels from json file to mLevels
        this.loadLevelsFromJsonFile();

    }

    public int getNumberOfLevels() {
        return mLevels.getmLevels().size();
    }

    public GameState build(int levelNumber) {

        Board board = new Board();
        Camera camera = new Camera();

        // TODO: do it better?
        Level level = mLevels.getmLevels().get(levelNumber - 1);

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

        ArrayList<Player> players = new ArrayList<>(computerPlayers);
        players.add(LevelLoader.humanPlayer);
        GameState state = new GameState(camera, board, players, LevelLoader.humanPlayer);

        // add AI to computer players
        for (Player computerPlayer : LevelLoader.computerPlayers) {
            computerPlayer.setAI(new RuleBasedAI(state));
        }


        return state;
    }

    private void validateJsonFile() {


    }

    private void loadLevelsFromJsonFile() {

        // initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Levels.class, new LevelDeserializer());
        Gson gson = gsonBuilder.create();

        InputStream is = mContext.getApplicationContext().getResources().openRawResource(LEVELS_RESOURCE_FILE);
        Reader r = new BufferedReader(new InputStreamReader(is));

        try {
            mLevels = gson.fromJson(r, Levels.class);
        } catch (Exception e) {
            Log.e("debugging", "error parsing json file");
        }

    }

}
