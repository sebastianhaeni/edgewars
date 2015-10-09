package ch.bfh.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

import ch.bfh.edgewars.R;
import ch.bfh.edgewars.logic.entities.board.node.Node;

public class DefenseDialog extends Dialog {

    public DefenseDialog(Context context, Node node) {
        super(context);

        setContentView(R.layout.dialog_defense);
    }

}
