package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Button;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

/**
 *
 */
public class NodeMenu {
    private final Node mNode;
    private final boolean mIsOwned;
    private boolean mVisible;
    private NodeButton mMeleeButton;
    private NodeButton mTankButton;
    private NodeButton mSprinterButton;

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
        mMeleeButton = new NodeButton(mNode.getPosition(), -1, -1, String.valueOf(mNode.getMeleeCount()), Constants.UNIT_MELEE_CORNERS);
        mTankButton = new NodeButton(mNode.getPosition(), 0, -1.5f, String.valueOf(mNode.getTankCount()), Constants.UNIT_TANK_CORNERS);
        mSprinterButton = new NodeButton(mNode.getPosition(), 1, -1, String.valueOf(mNode.getSprinterCount()), Constants.UNIT_SPRINTER_CORNERS);

        mMeleeButton.register();
        mTankButton.register();
        mSprinterButton.register();

        if (!mIsOwned) {
            return;
        }

        // adding click listeners
        mMeleeButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.askPlayerForTargetNode(EUnitType.MELEE);
            }
        });
        mTankButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.askPlayerForTargetNode(EUnitType.TANK);
            }
        });
        mSprinterButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.askPlayerForTargetNode(EUnitType.SPRINTER);
            }
        });
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void hide() {
        mMeleeButton.hide();
        mTankButton.hide();
        mSprinterButton.hide();

        mMeleeButton.unregister();
        mTankButton.unregister();
        mSprinterButton.register();

        mVisible = false;
    }
}
