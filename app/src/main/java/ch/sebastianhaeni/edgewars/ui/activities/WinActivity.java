package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityWinBinding;

public class WinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityWinBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_win,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());
    }

    public void back(View view) {
        Intent intent = new Intent(WinActivity.this, LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, getIntent().getExtras().getInt(LevelDetailActivity.LEVEL_ID));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
