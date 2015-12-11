package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.lang.reflect.Type;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.entities.Button;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.MeleeUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.SprinterUnit;
import ch.sebastianhaeni.edgewars.logic.entities.board.units.TankUnit;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 *
 */
public class NodeMenu {
    private final Node mNode;
    private final boolean mIsOwned;
    private boolean mVisible;
    private DraggableButton mMeleeButton;
    private DraggableButton mTankButton;
    private DraggableButton mSprinterButton;
    private NodeButton mMeleeFactoryButton;
    private NodeButton mSprinterFactoryButton;
    private NodeButton mTankFactoryButton;
    private NodeButton mRepairButton;
    private NodeButton mHealthButton;
    private NodeButton mDamageButton;
    private Polygon mUnitCorona;

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

    /**
     * Shows this menu.
     */
    public void show() {
        showUnits();
        showFactories();
        showUpgrades();
        mVisible = true;
    }

    /**
     * Shows upgrade buttons.
     */
    private void showUpgrades() {
        if (!mIsOwned) {
            return;
        }

        mRepairButton = new NodeButton(mNode.getPosition(), -1, 1, String.valueOf(Text.WRENCH), Constants.NODE_CORNERS);
        mRepairButton.register();
        mRepairButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.repair();
            }
        });

        mHealthButton = new NodeButton(mNode.getPosition(), 0, 1.5f, String.valueOf(Text.HEALTH), Constants.NODE_CORNERS);
        mHealthButton.register();
        mHealthButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.upgradeHealth();
            }
        });

        mDamageButton = new NodeButton(mNode.getPosition(), 1, 1, String.valueOf(Text.DAMAGE), Constants.NODE_CORNERS);
        mDamageButton.register();
        mDamageButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                mNode.upgradeDamage();
            }
        });
    }

    /**
     * Shows factory buttons.
     */
    private void showFactories() {
        if (!mIsOwned) {
            return;
        }

        if (!mNode.getMeleeFactory().maxLevelReached()) {
            mMeleeFactoryButton = new NodeButton(mMeleeButton.getPosition(), -.7f, -.7f, String.valueOf(Constants.FACTORY_MELEE_UPGRADE_1) + Text.ENERGY, Constants.NODE_CORNERS);
            mMeleeFactoryButton.register();
            mMeleeFactoryButton.addClickListener(new Button.OnGameClickListener() {
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
            mTankFactoryButton.addClickListener(new Button.OnGameClickListener() {
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
            mSprinterFactoryButton.addClickListener(new Button.OnGameClickListener() {
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

    /**
     * Shows unit buttons.
     */
    private void showUnits() {
        // fetching color of player
        float[] color = mNode.getState() instanceof OwnedState ? ((OwnedState) mNode.getState()).getOwner().getColor() : Colors.EDGE;

        // creating buttons
        mMeleeButton = new DraggableButton(mNode.getPosition(), -1, -1, String.valueOf(mNode.getMeleeCount()), Constants.UNIT_MELEE_CORNERS, color);
        mTankButton = new DraggableButton(mNode.getPosition(), 0, -1.5f, String.valueOf(mNode.getTankCount()), Constants.UNIT_TANK_CORNERS, color);
        mSprinterButton = new DraggableButton(mNode.getPosition(), 1, -1, String.valueOf(mNode.getSprinterCount()), Constants.UNIT_SPRINTER_CORNERS, color);

        mMeleeButton.register();
        mTankButton.register();
        mSprinterButton.register();

        if (!mIsOwned) {
            return;
        }

        // showing the selected unit that is built
        if (mNode.getBuildUnitType() == MeleeUnit.class) {
            setUnitBuildType(MeleeUnit.class, mMeleeButton);
        } else if (mNode.getBuildUnitType() == TankUnit.class) {
            setUnitBuildType(MeleeUnit.class, mTankButton);
        } else if (mNode.getBuildUnitType() == SprinterUnit.class) {
            setUnitBuildType(MeleeUnit.class, mSprinterButton);
        }

        // add drag listener that sends unit
        mMeleeButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.MELEE));
        mTankButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.TANK));
        mSprinterButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.SPRINTER));

        // adding click listeners
        mMeleeButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                setUnitBuildType(MeleeUnit.class, mMeleeButton);
            }
        });
        mTankButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                setUnitBuildType(TankUnit.class, mTankButton);
            }
        });
        mSprinterButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                setUnitBuildType(SprinterUnit.class, mSprinterButton);
            }
        });
    }

    /**
     * @return if this menu is visible or not
     */
    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Hides the node menu by hiding all drawables.
     */
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

        if (mUnitCorona != null) {
            mUnitCorona.unregister();
        }

        mVisible = false;
    }

    /**
     * @param button the button to hide
     */
    private static void hide(NodeButton button) {
        if (button == null) {
            return;
        }

        button.hide();
        button.unregister();
    }

    /**
     * @param unitClass  class of the unit that's the new build selection
     * @param nodeButton the button that must be highlighted
     */
    public void setUnitBuildType(Type unitClass, NodeButton nodeButton) {
        if (mUnitCorona != null) {
            mUnitCorona.unregister();
        }

        if (unitClass == mNode.getBuildUnitType()) {
            mNode.setBuildUnitType(null);
            return;
        }

        mUnitCorona = new Polygon(nodeButton.getPosition(), Colors.CORONA, 2, nodeButton.getPolygonCorners(), 0, .45f);
        mUnitCorona.register();

        mNode.setBuildUnitType(unitClass);
    }
}
