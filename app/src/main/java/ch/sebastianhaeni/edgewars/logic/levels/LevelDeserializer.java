package ch.sebastianhaeni.edgewars.logic.levels;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.Edge;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NodeState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.util.Position;

public class LevelDeserializer implements JsonDeserializer {

    private Map<Integer, Player> mPlayerMap;
    private Map<Integer, Node> mNodeMap;

    @Override
    public Levels deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Levels levels = new Levels();
        ArrayList<Level> levelList = new ArrayList<>();
        mPlayerMap = new HashMap<>();
        mNodeMap = new HashMap<>();

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray levelsArray = jsonObject.get("levels").getAsJsonArray();

        // iterate through all mLevels of json file and create level objects
        for (int i = 0; i < levelsArray.size(); i++) {
            JsonObject levelObject = levelsArray.get(i).getAsJsonObject();

            // create level
            Level level = new Level();
            int levelNumber = levelObject.get("level_number").getAsInt();
            level.setLevelNumber(levelNumber);

            // add players to level
            JsonArray playersArray = levelObject.get("players").getAsJsonArray();
            ArrayList<Player> playersList = this.createPlayers(playersArray);
            level.setPlayers(playersList);

            // add nodes to level
            JsonArray nodesArray = levelObject.get("nodes").getAsJsonArray();
            ArrayList<Node> nodesList = this.createNodes(nodesArray);
            level.setNodes(nodesList);

            // add edges to level
            JsonArray edgesArray = levelObject.get("edges").getAsJsonArray();
            ArrayList<Edge> edgesList = this.createEdges(edgesArray);
            level.setEdges(edgesList);

            levelList.add(level);
        }

        levels.setLevels(levelList);
        return levels;
    }

    private ArrayList<Player> createPlayers(JsonArray playersArray) throws JsonParseException {

        ArrayList<Player> levelPlayersList = new ArrayList<>();

        for (int i = 0; i < playersArray.size(); i++) {
            JsonObject playerObject = playersArray.get(i).getAsJsonObject();

            int playerId = playerObject.get("player_id").getAsInt();
            String playerNature = playerObject.get("nature").getAsString();

            Player player;

            switch (playerNature) {
                case "human":
                    player = LevelLoader.humanPlayer;
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

    private ArrayList<Node> createNodes(JsonArray nodesArray) throws JsonParseException {

        ArrayList<Node> levelNodesList = new ArrayList<>();

        for (int i = 0; i < nodesArray.size(); i++) {
            JsonObject nodeObject = nodesArray.get(i).getAsJsonObject();

            // set position of Node
            JsonObject position = nodeObject.get("position").getAsJsonObject();
            int coordinateX = position.get("coordinate_x").getAsInt();
            int coordinateY = position.get("coordinate_y").getAsInt();

            Node node = new Node(new Position(coordinateX, coordinateY));

            // add node to hash map (in order to match with edges below)
            int nodeId = nodeObject.get("node_id").getAsInt();
            mNodeMap.put(nodeId, node);

            // get initial values of Node
            JsonObject initialValuesObject = nodeObject.get("initial_values").getAsJsonObject();

            // set initial state of Node
            String stateString = initialValuesObject.get("state").getAsString();

            switch (stateString) {
                case "owned":
                    this.addInitialState(initialValuesObject, node);
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

    private ArrayList<Edge> createEdges(JsonArray edgesArray) throws JsonParseException {

        ArrayList<Edge> levelEdgesList = new ArrayList<>();

        for (int i = 0; i < edgesArray.size(); i++) {
            JsonObject edgeObject = edgesArray.get(i).getAsJsonObject();

            int node1_id = edgeObject.get("node1_id").getAsInt();
            Node node1 = mNodeMap.get(node1_id);

            int node2_id = edgeObject.get("node2_id").getAsInt();
            Node node2 = mNodeMap.get(node2_id);

            Edge edge = new Edge(node1, node2);

            levelEdgesList.add(edge);
        }

        return levelEdgesList;
    }

    private void addInitialState(JsonObject initialValuesObject, Node node) throws JsonParseException {

        // get owner player id
        int ownerId = initialValuesObject.get("owner_id").getAsInt();
        Player owner = mPlayerMap.get(ownerId);

        // create owned state and add it to the node
        NodeState state = new OwnedState(node, owner);
        node.setState(state);

        // get initial node units
        JsonObject units = initialValuesObject.get("units").getAsJsonObject();
        int meleeCount = units.get("melee_count").getAsInt();
        int sprinterCount = units.get("sprinter_count").getAsInt();
        int tankCount = units.get("tank_count").getAsInt();

        // add units to node
        for (int i = 0; i < meleeCount; i++) {
            node.addUnit(new MeleeUnit(node));
        }

        for (int i = 0; i < sprinterCount; i++) {
            node.addUnit(new SprinterUnit(node));
        }

        for (int i = 0; i < tankCount; i++) {
            node.addUnit(new TankUnit(node));
        }

        // get initial node factories
        JsonObject factories = initialValuesObject.get("factories").getAsJsonObject();
        JsonObject meleeFactoryObj = factories.get("melee_factory").getAsJsonObject();
        JsonObject sprinterFactoryObj = factories.get("sprinter_factory").getAsJsonObject();
        JsonObject tankFactoryObj = factories.get("tank_factory").getAsJsonObject();

        // check if factories are built
        boolean meleeFactoryBuilt = meleeFactoryObj.get("is_built").getAsBoolean();
        boolean sprinterFactoryBuilt = sprinterFactoryObj.get("is_built").getAsBoolean();
        boolean tankFactoryBuilt = tankFactoryObj.get("is_built").getAsBoolean();

        // get reference to node factories
        Factory meleeFactory = node.getMeleeFactory();
        Factory sprinterFactory = node.getSprinterFactory();
        Factory tankFactory = node.getTankFactory();

        // build factories and set levels_schema, if present
        if (meleeFactoryBuilt) {
            meleeFactory.build();
            int level = meleeFactoryObj.get("level").getAsInt();
            while (meleeFactory.getLevel() < level) {
                meleeFactory.upgrade();
            }
        }

        if (sprinterFactoryBuilt) {
            sprinterFactory.build();
            int level = sprinterFactoryObj.get("level").getAsInt();
            while (sprinterFactory.getLevel() < level) {
                sprinterFactory.upgrade();
            }
        }

        if (tankFactoryBuilt) {
            tankFactory.build();
            int level = tankFactoryObj.get("level").getAsInt();
            while (tankFactory.getLevel() < level) {
                tankFactory.upgrade();
            }
        }

    }
}