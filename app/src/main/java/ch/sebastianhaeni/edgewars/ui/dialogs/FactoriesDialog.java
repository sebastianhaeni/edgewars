package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;

public class FactoriesDialog extends Dialog {

    public FactoriesDialog(Context context, Node node) {
        super(context);

        setContentView(R.layout.dialog_factories);
    }

}
