package ch.sebastianhaeni.edgewars.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.DialogPauseBinding;
import ch.sebastianhaeni.edgewars.logic.GameThread;
import ch.sebastianhaeni.edgewars.ui.activities.GameActivity;

public class PauseDialog extends Dialog {

    private final Context mContext;

    public PauseDialog(Context context, final GameThread thread) {
        super(context);
        mContext = context;
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
        cancel();
    }

    public void exitGame(View view) {
        hide();
        ((GameActivity) mContext).finish();
    }

}
