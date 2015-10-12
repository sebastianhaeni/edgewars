package ch.bfh.edgewars.logic;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.edgewars.R;
import ch.bfh.edgewars.logic.ai.RuleBasedAI;
import ch.bfh.edgewars.logic.entities.Camera;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Board;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.levels.Level;
import ch.bfh.edgewars.logic.levels.Levels;
import ch.bfh.edgewars.logic.levels.LevelDeserializer;
import ch.bfh.edgewars.util.Colors;

public class LevelLoader {

    public static Player mHumanPlayer = new Player(Colors.BLUE);
    public static ArrayList<Player> mComputerPlayers = new ArrayList<>();

    private JsonParser parser;
    private Gson gson;
    private Levels levels;

    public static Player addComputerPlayer () {
        Player newComputerPlayer = new Player(Colors.RED);
        mComputerPlayers.add(newComputerPlayer);
        return newComputerPlayer;
    }

    public LevelLoader (Context context) {

        // initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Levels.class, new LevelDeserializer());
        gson = gsonBuilder.create();

        InputStream is = context.getApplicationContext().getResources().openRawResource(R.raw.levels);
        parser = new JsonParser();

        try (Reader r = new BufferedReader(new InputStreamReader(is))) {
            levels = gson.fromJson(r, Levels.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public GameState build (int levelNumber) {

        Board board = new Board();
        Camera camera = new Camera();

        // TODO: do it better
        Level level = levels.getLevels().get(levelNumber-1);

        List<Node> nodes = level.getNodes();
        List<Edge> edges = level.getEdges();

        // first add edges to board, so that they are drawn under nodes
        for (Edge edge : edges) {
            board.addEntity(edge);
        }

        // then add the nodes
        for (Node node : nodes) {
            board.addEntity(node);
        }

        GameState state = new GameState(camera, board, LevelLoader.mHumanPlayer);

        // add AI to computer players
        for (Player computerPlayer : LevelLoader.mComputerPlayers) {
            computerPlayer.setAI(new RuleBasedAI(state));
        }


        return state;
    }

}