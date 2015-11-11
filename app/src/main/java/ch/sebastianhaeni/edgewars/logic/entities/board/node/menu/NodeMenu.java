package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 *
 */
public class NodeMenu {
    private final Node mNode;
    private final boolean mIsOwned;
    private boolean mVisible;
    private NodeButton mMeleeButton;

    /**
     * Constructor
     *
     * @param node    the node this menu is for
     * @param isOwned if the player owns this node and can perform actions with it
     */
    public NodeMenu(Node node, boolean isOwned) {
        mNode = node;
        mIsOwned = isOwned;
    }

    public void show() {
        showUnits();
        showFactories();
        showUpgrades();
        mVisible = true;
    }

    private void showUpgrades() {

    }

    private void showFactories() {

    }

    private void showUnits() {
        mMeleeButton = new NodeButton(mNode.getPosition(), .5f, -.5f, String.valueOf(mNode.getMeleeCount()));

    }

    public boolean isVisible() {
        return mVisible;
    }

    public void hide() {
        mMeleeButton.hide();
        mVisible = false;
    }
}
