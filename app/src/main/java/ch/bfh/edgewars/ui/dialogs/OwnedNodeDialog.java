package ch.bfh.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;

import ch.bfh.edgewars.R;
import ch.bfh.edgewars.databinding.DialogOwnedNodeBinding;
import ch.bfh.edgewars.logic.Game;
import ch.bfh.edgewars.logic.commands.BuildMeleeFactoryCommand;
import ch.bfh.edgewars.logic.commands.BuildMeleeUnitCommand;
import ch.bfh.edgewars.logic.commands.BuildSprinterFactoryCommand;
import ch.bfh.edgewars.logic.commands.BuildSprinterUnitCommand;
import ch.bfh.edgewars.logic.commands.BuildTankFactoryCommand;
import ch.bfh.edgewars.logic.commands.BuildTankUnitCommand;
import ch.bfh.edgewars.logic.commands.UpgradeFactoryCommand;
import ch.bfh.edgewars.logic.commands.UpgradeNodeDamageCommand;
import ch.bfh.edgewars.logic.commands.UpgradeNodeHealthCommand;
import ch.bfh.edgewars.logic.entities.board.node.Node;

public class OwnedNodeDialog extends Dialog {

    private final DialogOwnedNodeBinding mBinding;
    private Node mNode;

    public OwnedNodeDialog(Context context, Node node) {
        super(context);
        setTitle("Owned Node");
        mNode = node;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_owned_node, null, false);
        mBinding.setNode(mNode);
        mBinding.setHandlers(new Handlers());
        setContentView(mBinding.getRoot());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mBinding.getRoot().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @SuppressWarnings("unused")
    public class Handlers {

        public void upgradeHealth(View view) {
            Game.getInstance().register(new UpgradeNodeHealthCommand(mNode));

            if (mNode.maxHealthLevelReached()) {
                mBinding.buttonUpgradeHealth.setVisibility(View.INVISIBLE);
                mBinding.textUpgradeHealthCost.setVisibility(View.INVISIBLE);
            }
        }

        public void upgradeDamage(View view) {
            Game.getInstance().register(new UpgradeNodeDamageCommand(mNode));

            if (mNode.maxDamageLevelReached()) {
                mBinding.buttonUpgradeDamage.setVisibility(View.INVISIBLE);
                mBinding.textUpgradeDamageCost.setVisibility(View.INVISIBLE);
            }
        }

        public void buildMeleeFactory() {
            Game.getInstance().register(new BuildMeleeFactoryCommand(mNode));
        }

        public void upgradeMeleeFactory() {
            Game.getInstance().register(new UpgradeFactoryCommand(mNode.getMeleeFactory()));
        }

        public void buildMeleeUnit(View view) {
            Game.getInstance().register(new BuildMeleeUnitCommand(mNode.getMeleeFactory()));
        }

        public void buildTankFactory() {
            Game.getInstance().register(new BuildTankFactoryCommand(mNode));
        }

        public void upgradeTankFactory() {
            Game.getInstance().register(new UpgradeFactoryCommand(mNode.getTankFactory()));
        }

        public void buildTankUnit(View view) {
            Game.getInstance().register(new BuildTankUnitCommand(mNode.getTankFactory()));
        }

        public void buildSprinterFactory() {
            Game.getInstance().register(new BuildSprinterFactoryCommand(mNode));
        }

        public void upgradeSprinterFactory() {
            Game.getInstance().register(new UpgradeFactoryCommand(mNode.getSprinterFactory()));
        }

        public void buildSprinterUnit(View view) {
            Game.getInstance().register(new BuildSprinterUnitCommand(mNode.getSprinterFactory()));
        }
    }
}