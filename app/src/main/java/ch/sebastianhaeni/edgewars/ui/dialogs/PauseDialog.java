package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

import ch.sebastianhaeni.edgewars.R;

public class PauseDialog extends Dialog {

    public PauseDialog(Context context) {
        super(context);

        setContentView(R.layout.dialog_pause);
    }

}
