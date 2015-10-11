package ch.bfh.edgewars.logic.levels;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import ch.bfh.edgewars.R;
import ch.bfh.edgewars.logic.entities.board.Board;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;

public class LevelLoader {

    private Context mContext;
    private JSONObject jsonObject;
    private JsonParser parser;
    private Gson gson;
    private Levels levels;

    public LevelLoader (Context context) {

        mContext = context;

        // initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Levels.class, new LevelsDeserializer());
        gson = gsonBuilder.create();

        InputStream is = context.getApplicationContext().getResources().openRawResource(R.raw.levels);
        parser = new JsonParser();

        try (Reader r = new BufferedReader(new InputStreamReader(is))) {

            levels = gson.fromJson(r, Levels.class);
            Log.i("test", "number of levels: "+levels.getLevels().size());


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public Board createBoard (int levelNumber) {

        Board board = new Board();
        Level level = levels.getLevels().get(levelNumber-1);

        List<Node> nodes = level.getNodes();
        List<Edge> edges = level.getEdges();

        // first add edges to board
        for (Edge edge : edges) {
            board.addEntity(edge);
        }

        // then add the nodes
        for (Node node : nodes) {
            board.addEntity(node);
        }

        return board;
    }

}
