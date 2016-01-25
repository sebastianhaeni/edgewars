package ch.sebastianhaeni.edgewars.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.databinding.ActivityLevelSelectionBinding;
import ch.sebastianhaeni.edgewars.logic.Constants;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.ui.fragments.ScreenSlidePageFragment;

@SuppressWarnings({"unused", "UnusedParameters"})
public class LevelSelectionActivity extends FragmentActivity {

    private ViewPager mPager;
    private int mLevelCount = Constants.LEVEL_COUNT_UNINITIALIZED;
    private static final String LEVEL_ID = "LEVEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLevelSelectionBinding binding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.activity_level_selection,
                null,
                false);
        binding.setActivity(this);
        setContentView(binding.getRoot());

        if (mLevelCount == Constants.LEVEL_COUNT_UNINITIALIZED) {
            mLevelCount = new LevelLoader(this).getLevelNumbers().size();
        }

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        // display the fragment which triggered the next Activity
        if (getIntent().hasExtra(LEVEL_ID)) {
            int lvlIndex = getIntent().getExtras().getInt(LEVEL_ID) - 1;
            mPager.setCurrentItem(lvlIndex);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mLevelCount;
        }
    }

    public void back(View view) {
        finish();
    }
}
