package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.DialogPauseBinding;
import ch.sebastianhaeni.edgewars.logic.GameThread;

public class PauseDialog extends Dialog {

    public PauseDialog(Context context, final GameThread thread) {
        super(context);

        DialogPauseBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_pause,
                null,
                false);

        binding.setDialog(this);
        setContentView(binding.getRoot());

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
