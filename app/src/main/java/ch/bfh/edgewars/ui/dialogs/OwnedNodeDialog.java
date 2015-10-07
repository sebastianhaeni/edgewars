package ch.bfh.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;

import ch.bfh.edgewars.R;
import ch.bfh.edgewars.databinding.DialogOwnedNodeBinding;
import ch.bfh.edgewars.logic.Game;
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
            mNode.upgradeDamage();
            if (mNode.maxDamageLevelReached()) {
                mBinding.buttonUpgradeDamage.setVisibility(View.INVISIBLE);
                mBinding.textUpgradeDamageCost.setVisibility(View.INVISIBLE);
            }
        }

        public void buildMeleeFactory() {
            mNode.getMeleeFactory().build();
        }

        public void upgradeMeleeFactory() {
            mNode.getMeleeFactory().upgrade();
        }

        public void buildMeleeUnit(View view) {
            mNode.getMeleeFactory().buildUnit();
        }

        public void buildTankFactory() {
            mNode.getTankFactory().build();
        }

        public void upgradeTankFactory() {
            mNode.getTankFactory().upgrade();
        }

        public void buildTankUnit(View view) {
            mNode.getTankFactory().buildUnit();
        }

        public void buildSprinterFactory() {
            mNode.getSprinterFactory().build();
        }

        public void upgradeSprinterFactory() {
            mNode.getSprinterFactory().upgrade();
        }

        public void buildSprinterUnit(View view) {
            mNode.getSprinterFactory().buildUnit();
        }
    }
}
