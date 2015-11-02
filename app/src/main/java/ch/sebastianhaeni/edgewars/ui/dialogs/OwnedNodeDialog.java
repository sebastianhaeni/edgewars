package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.Window;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.DialogOwnedNodeBinding;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.commands.BuildMeleeFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.BuildMeleeUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.BuildSprinterFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.BuildSprinterUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.BuildTankFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.BuildTankUnitCommand;
import ch.sebastianhaeni.edgewars.logic.commands.RepairNodeCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeFactoryCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeNodeDamageCommand;
import ch.sebastianhaeni.edgewars.logic.commands.UpgradeNodeHealthCommand;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.ui.GameController;

public class OwnedNodeDialog extends Dialog {

    private final DialogOwnedNodeBinding mBinding;
    private final Node mNode;
    private final GameController mController;

    public OwnedNodeDialog(Context context, Node node, GameController controller) {
        super(context);
        mNode = node;
        mController = controller;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_owned_node, null, false);
        mBinding.setNode(mNode);
        mBinding.setDialog(this);
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

    public void upgradeHealth(View view) {
        Game.getInstance().register(new UpgradeNodeHealthCommand(mNode));
    }

    public void upgradeDamage(View view) {
        Game.getInstance().register(new UpgradeNodeDamageCommand(mNode));
    }

    public void sendMelee(View view) {
        mController.askPlayerForTargetNode(mNode, EUnitType.MELEE);
        dismiss();
    }

    public void sendTank(View view) {
        mController.askPlayerForTargetNode(mNode, EUnitType.TANK);
        dismiss();
    }

    public void sendSprinter(View view) {
        mController.askPlayerForTargetNode(mNode, EUnitType.SPRINTER);
        dismiss();
    }

    public void buildMeleeFactory() {
        Game.getInstance().register(new BuildMeleeFactoryCommand(mNode));
    }

    public void upgradeMeleeFactory(View view) {
        Game.getInstance().register(new UpgradeFactoryCommand(mNode.getMeleeFactory()));
    }

    public void buildMeleeUnit(View view) {
        Game.getInstance().register(new BuildMeleeUnitCommand(mNode.getMeleeFactory()));
    }

    public void buildTankFactory() {
        Game.getInstance().register(new BuildTankFactoryCommand(mNode));
    }

    public void upgradeTankFactory(View view) {
        Game.getInstance().register(new UpgradeFactoryCommand(mNode.getTankFactory()));
    }

    public void buildTankUnit(View view) {
        Game.getInstance().register(new BuildTankUnitCommand(mNode.getTankFactory()));
    }

    public void buildSprinterFactory() {
        Game.getInstance().register(new BuildSprinterFactoryCommand(mNode));
    }

    public void upgradeSprinterFactory(View view) {
        Game.getInstance().register(new UpgradeFactoryCommand(mNode.getSprinterFactory()));
    }

    public void buildSprinterUnit(View view) {
        Game.getInstance().register(new BuildSprinterUnitCommand(mNode.getSprinterFactory()));
    }

    public void repairNode(View view) {
        Game.getInstance().register(new RepairNodeCommand(mNode));
    }

    public void dismiss(View view) {
        dismiss();
    }
}
