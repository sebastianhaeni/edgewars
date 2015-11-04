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
import ch.sebastianhaeni.edgewars.logic.levels.Level;
import ch.sebastianhaeni.edgewars.logic.levels.LevelDeserializer;
import ch.sebastianhaeni.edgewars.logic.levels.LevelNumberDeserializer;

public class LevelLoader {

    public static int levelNumber = 1;

    private final Context mContext;
    private Level mLevel;
    private ArrayList<Integer> mLevelNumbers;

    public LevelLoader(Context context) {

        mContext = context;

        // load level numbers from json file to mLevelNumbers
        this.loadLevelNumbersFromJsonFile();
    }

    public ArrayList<Integer> getLevelNumbers() {
        return mLevelNumbers;
    }

    public GameState build(int levelNr) throws IllegalArgumentException {

        // ensure that the specified level number does exist
        if (!mLevelNumbers.contains(levelNr)) {
            throw new IllegalArgumentException("A level with the specified number (" + levelNr + ") was not found!");
        }

        levelNumber = levelNr;
        this.loadLevelFromJsonFile();

        Board board = new Board();
        Camera camera = new Camera();

        ArrayList<Node> nodes = mLevel.getNodes();
        ArrayList<Edge> edges = mLevel.getEdges();

        // first add edges to board, so that they are drawn under nodes
        for (Edge edge : edges) {
            board.addEntity(edge);
        }

        // then add the nodes
        for (Node node : nodes) {
            board.addEntity(node);
        }

        // get human player
        Player humanPlayer = mLevel.getHumanPlayers().get(0);

        // get all players of level and add to array list
        ArrayList<Player> allPlayers = new ArrayList<>();
        ArrayList<Player> computerPlayers = mLevel.getComputerPlayers();
        allPlayers.addAll(computerPlayers);
        allPlayers.add(humanPlayer);

        GameState state = new GameState(camera, board, allPlayers, humanPlayer);

        // add AI to computer players
        for (Player computerPlayer : computerPlayers) {
            computerPlayer.setAI(new RuleBasedAI(state, computerPlayer));
        }

        return state;
    }

    private void loadLevelNumbersFromJsonFile() {
        // initialize gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ArrayList.class, new LevelNumberDeserializer());
        Gson gson = gsonBuilder.create();

        InputStream is = mContext.getApplicationContext().getResources().openRawResource(R.raw.levels);
        Reader r = new BufferedReader(new InputStreamReader(is));

        //noinspection unchecked
        mLevelNumbers = gson.fromJson(r, ArrayList.class);
    }

    private void loadLevelFromJsonFile() {
        // initialize gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Level.class, new LevelDeserializer());
        Gson gson = gsonBuilder.create();

        InputStream is = mContext.getApplicationContext().getResources().openRawResource(R.raw.levels);
        Reader r = new BufferedReader(new InputStreamReader(is));

        mLevel = gson.fromJson(r, Level.class);
    }

}
