package ch.sebastianhaeni.edgewars.logic.entities.board.node.menu;

import java.util.Observable;
import java.util.Observer;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.ActivateFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.DeactivateFactoriesCommand;
import ch.sebastianhaeni.edgewars.logic.commands.RepairNodeCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeNodeDamageCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeNodeHealthCommand;
import ch.sebastianhaeni.edgewars.logic.entities.Button;
import ch.sebastianhaeni.edgewars.logic.entities.Player;
import ch.sebastianhaeni.edgewars.logic.entities.board.factories.Factory;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.util.Colors;

/**
 * The menu around a node. Depending if the node is owned or not by the current player, it shows
 * more or less buttons. This class contains a lot of UI building code, so expect spaghetti code.
 * There's not much one can do to prevent that.
 */
public class NodeMenu extends Observable implements Observer {
    private final Node mNode;
    private final boolean mIsOwned;
    private boolean mVisible;
    private DraggableButton mMeleeButton;
    private DraggableButton mTankButton;
    private DraggableButton mSprinterButton;
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

        showUnits();
        showFactories();
        showUpgrades();
    }

    /**
     * Shows this menu.
     */
    public void show() {
        mVisible = true;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Shows upgrade buttons.
     */
    private void showUpgrades() {
        if (!mIsOwned) {
            return;
        }

        final Player owner = ((OwnedState) mNode.getState()).getOwner();

        NodeButton mRepairButton = new NodeButton(mNode.getPosition(), -1, 1, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && mNode.getHealth() < mNode.getMaxHealth() && owner.getEnergy() > 0;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Text.WRENCH);
            }
        }, Constants.NODE_CORNERS);
        mRepairButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new RepairNodeCommand(mNode));
            }
        });

        NodeButton mHealthButton = new NodeButton(mNode.getPosition(), 0, 1.5f, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && !mNode.maxHealthLevelReached() && owner.getEnergy() >= Constants.NODE_HEALTH_LEVEL_UPGRADE_COST;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Text.HEALTH);
            }
        }, Constants.NODE_CORNERS);
        mHealthButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new UpgradeNodeHealthCommand(mNode));
            }
        });

        NodeButton mDamageButton = new NodeButton(mNode.getPosition(), 1, 1, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && !mNode.maxDamageLevelReached() && owner.getEnergy() >= Constants.NODE_DAMAGE_LEVEL_UPGRADE_COST;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Text.DAMAGE);
            }
        }, Constants.NODE_CORNERS);
        mDamageButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new UpgradeNodeDamageCommand(mNode));
            }
        });

        mNode.addObserver(mRepairButton);
        mNode.addObserver(mHealthButton);
        mNode.addObserver(mDamageButton);
        owner.addObserver(mRepairButton);
        owner.addObserver(mHealthButton);
        owner.addObserver(mDamageButton);
        addObserver(mRepairButton);
        addObserver(mHealthButton);
        addObserver(mDamageButton);
    }

    /**
     * Shows factory buttons.
     */
    private void showFactories() {
        if (!mIsOwned) {
            return;
        }

        final Player owner = ((OwnedState) mNode.getState()).getOwner();

        NodeButton mMeleeFactoryButton = new NodeButton(mMeleeButton.getPosition(), -.7f, -.7f, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && mNode.getMeleeFactory().notFullyUpgraded() && owner.getEnergy() >= mNode.getMeleeFactory().getUpgradeCost();
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Constants.FACTORY_MELEE_UPGRADE_1) + Text.ENERGY;
            }
        }, Constants.NODE_CORNERS);
        mMeleeFactoryButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new UpgradeFactoryCommand(mNode.getMeleeFactory()));
            }
        });

        NodeButton mTankFactoryButton = new NodeButton(mTankButton.getPosition(), 0, -1, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && mNode.getTankFactory().notFullyUpgraded() && owner.getEnergy() >= mNode.getTankFactory().getUpgradeCost();
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Constants.FACTORY_TANK_UPGRADE_1) + Text.ENERGY;
            }
        }, Constants.NODE_CORNERS);
        mTankFactoryButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new UpgradeFactoryCommand(mNode.getTankFactory()));
            }
        });

        NodeButton mSprinterFactoryButton = new NodeButton(mSprinterButton.getPosition(), .7f, -.7f, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible && mNode.getSprinterFactory().notFullyUpgraded() && owner.getEnergy() >= mNode.getSprinterFactory().getUpgradeCost();
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(Constants.FACTORY_SPRINTER_UPGRADE_1) + Text.ENERGY;
            }
        }, Constants.NODE_CORNERS);
        mSprinterFactoryButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                Game.getInstance().register(new UpgradeFactoryCommand(mNode.getSprinterFactory()));
            }
        });

        mNode.getMeleeFactory().addObserver(mMeleeFactoryButton);
        mNode.getTankFactory().addObserver(mTankFactoryButton);
        mNode.getSprinterFactory().addObserver(mSprinterFactoryButton);
        owner.addObserver(mMeleeFactoryButton);
        owner.addObserver(mTankFactoryButton);
        owner.addObserver(mSprinterFactoryButton);
        addObserver(mMeleeFactoryButton);
        addObserver(mTankFactoryButton);
        addObserver(mSprinterFactoryButton);
    }

    /**
     * Shows unit buttons.
     */
    private void showUnits() {
        // fetching color of player
        Player owner = ((OwnedState) mNode.getState()).getOwner();
        float[] color = owner.getColor();

        // creating buttons
        mMeleeButton = new DraggableButton(mNode.getPosition(), -1, -1, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(mNode.getMeleeCount());
            }
        }, Constants.UNIT_MELEE_CORNERS, color);

        mTankButton = new DraggableButton(mNode.getPosition(), 0, -1.5f, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(mNode.getTankCount());
            }
        }, Constants.UNIT_TANK_CORNERS, color);

        mSprinterButton = new DraggableButton(mNode.getPosition(), 1, -1, new NodeButton.VisibleResolver() {
            @Override
            public boolean isVisible() {
                return mVisible;
            }
        }, new NodeButton.ButtonTextResolver() {
            @Override
            public String getText() {
                return String.valueOf(mNode.getSprinterCount());
            }
        }, Constants.UNIT_SPRINTER_CORNERS, color);

        mNode.addObserver(mMeleeButton);
        mNode.addObserver(mTankButton);
        mNode.addObserver(mSprinterButton);
        mNode.getMeleeFactory().addObserver(mMeleeButton);
        mNode.getTankFactory().addObserver(mTankButton);
        mNode.getSprinterFactory().addObserver(mSprinterButton);
        addObserver(mMeleeButton);
        addObserver(mTankButton);
        addObserver(mSprinterButton);
        mNode.addObserver(this);

        // showing the selected unit that is built
        highlightActiveFactory();

        if (!mIsOwned) {
            return;
        }

        // add drag listener that sends unit
        mMeleeButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.MELEE));
        mTankButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.TANK));
        mSprinterButton.addDragListener(new SendUnitDragListener(mNode, EUnitType.SPRINTER));

        // adding click listeners
        mMeleeButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                activateFactory(mNode.getMeleeFactory());
                markFactoryAsActive(mMeleeButton);
            }
        });
        mTankButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                activateFactory(mNode.getTankFactory());
                markFactoryAsActive(mTankButton);
            }
        });
        mSprinterButton.addClickListener(new Button.OnGameClickListener() {
            @Override
            public void onClick() {
                activateFactory(mNode.getSprinterFactory());
                markFactoryAsActive(mSprinterButton);
            }
        });
    }

    /**
     * Marks the active factory with a highlight corona.
     */
    private void highlightActiveFactory() {
        if (mNode.getMeleeFactory().isActivated()) {
            markFactoryAsActive(mMeleeButton);
        } else if (mNode.getTankFactory().isActivated()) {
            markFactoryAsActive(mTankButton);
        } else if (mNode.getSprinterFactory().isActivated()) {
            markFactoryAsActive(mSprinterButton);
        }
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
        if (mUnitCorona != null) {
            mUnitCorona.unregister();
        }

        mVisible = false;
        setChanged();
        notifyObservers(this);
    }

    /**
     * @param factory the unit factory to be activated
     */
    private void activateFactory(Factory factory) {
        if (mUnitCorona != null) {
            mUnitCorona.unregister();
        }

        if (factory.isActivated()) {
            Game.getInstance().register(new DeactivateFactoriesCommand(factory.getNode()));
            return;
        }

        Game.getInstance().register(new ActivateFactoryCommand(factory));
    }

    /**
     * Marks the node button as active by adding a glowing border.
     *
     * @param nodeButton the button that must be highlighted
     */
    private void markFactoryAsActive(NodeButton nodeButton) {
        if (!mVisible) {
            return;
        }

        if (mUnitCorona != null) {
            mUnitCorona.unregister();
        }

        mUnitCorona = new Polygon(nodeButton.getPosition(), Colors.CORONA, 2, nodeButton.getPolygonCorners(), 0, .45f);
        mUnitCorona.register();
    }

    @Override
    public void update(Observable observable, Object data) {
        highlightActiveFactory();
    }
}
