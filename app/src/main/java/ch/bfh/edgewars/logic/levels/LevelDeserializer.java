package ch.bfh.edgewars.logic.levels;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bfh.edgewars.logic.LevelLoader;
import ch.bfh.edgewars.logic.entities.Player;
import ch.bfh.edgewars.logic.entities.board.Edge;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.node.state.NeutralState;
import ch.bfh.edgewars.logic.entities.board.node.state.NodeState;
import ch.bfh.edgewars.logic.entities.board.node.state.OwnedState;
import ch.bfh.edgewars.logic.entities.board.units.MeleeUnit;
import ch.bfh.edgewars.logic.entities.board.units.SprinterUnit;
import ch.bfh.edgewars.logic.entities.board.units.TankUnit;
import ch.bfh.edgewars.util.Position;


public class LevelDeserializer implements JsonDeserializer {

    private Levels mLevels;
    private List<Level> mLevelList;
    private Map mPlayerMap;
    private Map mNodeMap;

    @Override
    public Levels deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        mLevels = new Levels();
        mLevelList = new ArrayList<>();
        mPlayerMap = new HashMap();
        mNodeMap = new HashMap();

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray levelsArray = jsonObject.get("levels").getAsJsonArray();

        // iterate through all mLevels of json file and create level objects
        for (int i=0; i<levelsArray.size(); i++) {
            JsonObject levelObject = levelsArray.get(i).getAsJsonObject();

            // create level
            Level level = new Level();
            int levelNumber = levelObject.get("level_number").getAsInt();
            level.setLevelNumber(levelNumber);

            // add players to level
            JsonArray playersArray = levelObject.get("players").getAsJsonArray();
            List<Player> playersList = this.createPlayers(playersArray);
            level.setPlayers(playersList);

            // add nodes to level
            JsonArray nodesArray = levelObject.get("nodes").getAsJsonArray();
            List<Node> nodesList =  this.createNodes(nodesArray);
            level.setNodes(nodesList);

            // add edges to level
            JsonArray edgesArray = levelObject.get("edges").getAsJsonArray();
            List<Edge> edgesList = createEdges(edgesArray);
            level.setmEdges(edgesList);

            mLevelList.add(level);
        }

        mLevels.setLevels(mLevelList);
        return mLevels;
    }

    private List<Player> createPlayers (JsonArray playersArray) {

        List<Player> levelPlayersList = new ArrayList<>();

        for (int i=0; i<playersArray.size(); i++) {
            JsonObject playerObject = playersArray.get(i).getAsJsonObject();

            int playerId = playerObject.get("player_id").getAsInt();
            String playerNature = playerObject.get("nature").getAsString();

            Player player;

            switch (playerNature) {
                case "human":
                    player = LevelLoader.mHumanPlayer;
                    break;
                default: // computer player
                    player = LevelLoader.addComputerPlayer();
                    break;
            }

            // add player to hash map (in order to match with nodes below)
            mPlayerMap.put(playerId, player);

            levelPlayersList.add(player);

        }

        return levelPlayersList;
    }

    private List<Node> createNodes (JsonArray nodesArray) {

        List<Node> levelNodesList = new ArrayList<>();

        for (int i=0; i<nodesArray.size(); i++) {
            JsonObject nodeObject = nodesArray.get(i).getAsJsonObject();

            Node node = new Node();

            // add node to hash map (in order to match with edges below)
            int nodeId = nodeObject.get("node_id").getAsInt();
            mNodeMap.put(nodeId, node);

            // set position of Node
            JsonObject position = nodeObject.get("position").getAsJsonObject();
            int coordinateX = position.get("coordinate_x").getAsInt();
            int coordinateY = position.get("coordinate_y").getAsInt();
            node.setPosition(new Position(coordinateX, coordinateY));

            // set initial state of Node
            String stateString = nodeObject.get("initial_state").getAsString();

            switch (stateString) {
                case "owned":
                    this.addInitialState(nodeObject, node);
                    break;
                default: // neutral node
                    NodeState state = new NeutralState(node);
                    node.setState(state);
                    break;
            }

            levelNodesList.add(node);
        }

        return levelNodesList;
    }

    private List<Edge> createEdges (JsonArray edgesArray) {

        List<Edge> levelEdgesList = new ArrayList<>();

        for (int i=0; i<edgesArray.size(); i++) {
            JsonObject edgeObject = edgesArray.get(i).getAsJsonObject();

            Edge edge = new Edge();

            int node1_id = edgeObject.get("node1_id").getAsInt();
            Node node1 = (Node) mNodeMap.get(node1_id);

            int node2_id = edgeObject.get("node2_id").getAsInt();
            Node node2 = (Node) mNodeMap.get(node2_id);

            edge.setNodes(node1, node2);

            levelEdgesList.add(edge);
        }

        return levelEdgesList;
    }

    private void addInitialState (JsonObject nodeObject, Node node) {
        JsonObject ownerDetails = nodeObject.get("owner_details").getAsJsonObject();

        // get owner player id
        int ownerId = ownerDetails.get("owner_id").getAsInt();
        Player owner = (Player) mPlayerMap.get(ownerId);

        // create owned state and add it to the node
        NodeState state = new OwnedState(node, owner);
        node.setState(state);

        // get initial node units
        JsonObject unitCount = ownerDetails.get("unit_count").getAsJsonObject();
        int meleeCount = unitCount.get("melee").getAsInt();
        int sprinterCount = unitCount.get("sprinter").getAsInt();
        int tankCount = unitCount.get("tank").getAsInt();

        Log.i("test", meleeCount+" meleeUnits, "+sprinterCount+" sprinterUnits, "+tankCount+" tank units for player "+ownerId);

        // create initial units
        ArrayList<MeleeUnit> meleeUnits = new ArrayList<>();
        ArrayList<SprinterUnit> sprinterUnits = new ArrayList<>();
        ArrayList<TankUnit> tankUnits = new ArrayList<>();

        // add units to node
        for (int i=0; i<meleeCount; i++)
            meleeUnits.add(new MeleeUnit(node));

        for (int i=0; i<sprinterCount; i++)
            sprinterUnits.add(new SprinterUnit(node));

        for (int i=0; i<tankCount; i++)
            tankUnits.add(new TankUnit(node));

    }
}
