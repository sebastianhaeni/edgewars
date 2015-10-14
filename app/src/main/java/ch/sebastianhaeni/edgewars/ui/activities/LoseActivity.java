package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityAboutBinding;
import ch.sebastianhaeni.edgewars.databinding.ActivityLoseBinding;

public class LoseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoseBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_lose,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());
    }

}
