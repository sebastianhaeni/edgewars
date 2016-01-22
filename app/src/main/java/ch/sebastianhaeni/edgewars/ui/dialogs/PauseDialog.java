package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.logic.GameThread;

public class PauseDialog extends Dialog {

    public PauseDialog(Context context, final GameThread thread) {
        super(context);

        setContentView(R.layout.dialog_pause);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                thread.onResume();
            }
        });
    }

    public void resumeGame(View view) {
        hide();
    }

    public void exitGame(View view) {
        onBackPressed();
    }

}
