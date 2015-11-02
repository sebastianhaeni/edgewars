package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.Window;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.DialogOpponentNodeBinding;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class OpponentNodeDialog extends Dialog {
    private final DialogOpponentNodeBinding mBinding;
    private final Node mNode;

    public OpponentNodeDialog(Context context, Node node) {
        super(context);

        mNode = node;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_opponent_node, null, false);
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

    public void dismiss(View view) {
        dismiss();
    }
}
