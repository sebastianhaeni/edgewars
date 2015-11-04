package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityAboutBinding;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAboutBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_about,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());
    }

    public void back(View view) {
        finish();
    }

}