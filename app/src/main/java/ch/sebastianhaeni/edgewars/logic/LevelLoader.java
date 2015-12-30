package ch.sebastianhaeni.edgewars.logic;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.sebastianhaeni.edgewars.logic.ai.RuleBasedAI;
import ch.sebastianhaeni.edgewars.logic.entities.Camera;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Board;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.levels.Level;
import ch.sebastianhaeni.edgewars.logic.levels.LevelDeserializer;

public class LevelLoader {

    private final static String LEVELS_FOLDER = "levels";
    private final static String TAG = "LevelLoader";

    private final Context mContext;
    private final Map<Integer, String> mLevels;

    public LevelLoader(Context context) {

        mContext = context;
        mLevels = new HashMap<>();

        // load level numbers and json file names to mLevels
        try {
            this.loadLevelInfo();
        } catch (Exception e) {
            Log.d(TAG, "Level Numbers could not be loaded.");
        }
    }

    public ArrayList<Integer> getLevelNumbers() {
        Set<Integer> levelNrSet = mLevels.keySet();
        ArrayList<Integer> levelNumbers = new ArrayList<>();
        levelNumbers.addAll(levelNrSet);
        Collections.sort(levelNumbers);

        return levelNumbers;
    }

    public GameState build(int levelNr) throws RuntimeException {

        // ensure that the specified level number does exist
        if (!mLevels.containsKey(levelNr)) {
            throw new IllegalArgumentException("A level with the specified number (" + levelNr + ") was not found!");
        }

        Level level;

        try {
            level = this.loadLevel(levelNr);
        } catch (IOException e) {
            throw new RuntimeException("The specified level " + levelNr + " could not be loaded from JSON File.");
        }

        Board board = new Board();
        Camera camera = new Camera();

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

        // get human player
        Player humanPlayer = level.getHumanPlayers().get(0);

        // get all players of level and add to array list
        ArrayList<Player> allPlayers = new ArrayList<>();
        ArrayList<Player> computerPlayers = level.getComputerPlayers();
        allPlayers.addAll(computerPlayers);
        allPlayers.add(humanPlayer);

        GameState state = new GameState(camera, board, allPlayers, humanPlayer);

        // add AI to computer players
        for (Player computerPlayer : computerPlayers) {
            computerPlayer.setAI(new RuleBasedAI(computerPlayer));
        }

        return state;
    }

    private void loadLevelInfo() throws IOException, NumberFormatException {
        AssetManager assetManager = mContext.getAssets();
        String[] fileNames = assetManager.list(LEVELS_FOLDER);

        // loop through all level asset files and add level nr + file name to mLevels
        for (String fileName : fileNames) {
            int levelNr = Integer.parseInt(fileName.replaceAll("[\\D]", ""));
            mLevels.put(levelNr, fileName);
        }

    }

    private Level loadLevel(int levelNr) throws IOException {
        String path = LEVELS_FOLDER + "/" + mLevels.get(levelNr);
        AssetManager assetManager = mContext.getAssets();
        InputStream is = assetManager.open(path);

        // initialize gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Level.class, new LevelDeserializer());
        Gson gson = gsonBuilder.create();

        Reader r = new BufferedReader(new InputStreamReader(is));

        return gson.fromJson(r, Level.class);
    }

}
