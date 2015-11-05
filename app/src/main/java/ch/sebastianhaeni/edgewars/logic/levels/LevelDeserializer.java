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
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

public class LevelDeserializer implements JsonDeserializer {

    private Map<Integer, Player> mPlayerMap;
    private Map<Integer, Node> mNodeMap;

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Level level = new Level();
        mPlayerMap = new HashMap<>();
        mNodeMap = new HashMap<>();

        JsonObject levelObject = json.getAsJsonObject();

        // add computer players to level
        JsonArray playersArray = levelObject.get("players").getAsJsonArray();
        ArrayList<Player> computerPlayersList = this.createPlayers(playersArray, "computer", Colors.NODE_OPPONENT);
        level.setComputerPlayers(computerPlayersList);

        // add human player(s) to level
        ArrayList<Player> humanPlayersList = this.createPlayers(playersArray, "human", Colors.NODE_MINE);
        level.setHumanPlayers(humanPlayersList);

        // add nodes to level
        JsonArray nodesArray = levelObject.get("nodes").getAsJsonArray();
        ArrayList<Node> nodesList = this.createNodes(nodesArray);
        level.setNodes(nodesList);

        // add edges to level
        JsonArray edgesArray = levelObject.get("edges").getAsJsonArray();
        ArrayList<Edge> edgesList = this.createEdges(edgesArray);
        level.setEdges(edgesList);


        return level;
    }

    private ArrayList<Player> createPlayers(JsonArray playersArray, String nature, float[] color) throws JsonParseException {

        ArrayList<Player> levelPlayersList = new ArrayList<>();

        for (int i = 0; i < playersArray.size(); i++) {
            JsonObject playerObject = playersArray.get(i).getAsJsonObject();

            int playerId = playerObject.get("player_id").getAsInt();
            String playerNature = playerObject.get("nature").getAsString();

            Player player;

            if (playerNature.equals(nature)) {
                player = new Player(color, nature.equals("human"));
            } else {
                continue;
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
        node.addUnit(new MeleeUnit(meleeCount, node, owner));
        node.addUnit(new SprinterUnit(sprinterCount, node, owner));
        node.addUnit(new TankUnit(tankCount, node, owner));

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
