package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
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
    private NodeButton mMeleeFactoryButton;
    private NodeButton mSprinterFactoryButton;
    private NodeButton mTankFactoryButton;
    private NodeButton mRepairButton;
    private NodeButton mHealthButton;
    private NodeButton mDamageButton;

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
        if (!mIsOwned) {
            return;
        }

        mRepairButton = new NodeButton(mNode.getPosition(), -1, 1, String.valueOf(Text.WRENCH), Constants.NODE_CORNERS);
        mRepairButton.register();
        mRepairButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.repair();
            }
        });

        mHealthButton = new NodeButton(mNode.getPosition(), 0, 1.5f, String.valueOf(Text.HEALTH), Constants.NODE_CORNERS);
        mHealthButton.register();
        mHealthButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.upgradeHealth();
            }
        });

        mDamageButton = new NodeButton(mNode.getPosition(), 1, 1, String.valueOf(Text.DAMAGE), Constants.NODE_CORNERS);
        mDamageButton.register();
        mDamageButton.addListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.upgradeDamage();
            }
        });

    }

    private void showFactories() {
        if (!mIsOwned) {
            return;
        }

        if (!mNode.getMeleeFactory().maxLevelReached()) {
            mMeleeFactoryButton = new NodeButton(mMeleeButton.getPosition(), -.7f, -.7f, String.valueOf(Constants.FACTORY_MELEE_UPGRADE_1) + Text.ENERGY, Constants.NODE_CORNERS);
            mMeleeFactoryButton.register();
            mMeleeFactoryButton.addListener(new Button.OnGameClickListener() {
                @Override
                public void onClick() {
                    mNode.getMeleeFactory().upgrade();
                    if (mNode.getMeleeFactory().maxLevelReached()) {
                        mMeleeFactoryButton.hide();
                        mMeleeFactoryButton.unregister();
                    }
                }
            });
        }

        if (!mNode.getTankFactory().maxLevelReached()) {
            mTankFactoryButton = new NodeButton(mTankButton.getPosition(), 0, -1, String.valueOf(Constants.FACTORY_TANK_UPGRADE_1) + Text.ENERGY, Constants.NODE_CORNERS);
            mTankFactoryButton.register();
            mTankFactoryButton.addListener(new Button.OnGameClickListener() {
                @Override
                public void onClick() {
                    mNode.getTankFactory().upgrade();
                    if (mNode.getTankFactory().maxLevelReached()) {
                        mTankFactoryButton.hide();
                        mTankFactoryButton.unregister();
                    }
                }
            });
        }

        if (!mNode.getSprinterFactory().maxLevelReached()) {
            mSprinterFactoryButton = new NodeButton(mSprinterButton.getPosition(), .7f, -.7f, String.valueOf(Constants.FACTORY_SPRINTER_UPGRADE_1) + Text.ENERGY, Constants.NODE_CORNERS);
            mSprinterFactoryButton.register();
            mSprinterFactoryButton.addListener(new Button.OnGameClickListener() {
                @Override
                public void onClick() {
                    mNode.getSprinterFactory().upgrade();
                    if (mNode.getSprinterFactory().maxLevelReached()) {
                        mSprinterFactoryButton.hide();
                        mSprinterFactoryButton.unregister();
                    }
                }
            });
        }
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
        hide(mMeleeButton);
        hide(mTankButton);
        hide(mSprinterButton);

        hide(mMeleeFactoryButton);
        hide(mTankFactoryButton);
        hide(mSprinterFactoryButton);

        hide(mRepairButton);
        hide(mHealthButton);
        hide(mDamageButton);

        mVisible = false;
    }

    private static void hide(NodeButton button) {
        if (button == null) {
            return;
        }

        button.hide();
        button.unregister();
    }
}
