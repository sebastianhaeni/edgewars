package ch.sebastianhaeni.edgewars.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.sebastianhaeni.edgewars.R;
import ch.sebastianhaeni.edgewars.logic.LevelLoader;
import ch.sebastianhaeni.edgewars.model.LevelRecord;
import ch.sebastianhaeni.edgewars.ui.activities.LevelDetailActivity;

/**
 * Fragment for displaying inside LevelSelection Activity
 */
public class ScreenSlidePageFragment extends Fragment {

    private int mLevelIndex;
    private LevelRecord mLevelRecord;

    // newInstance constructor for creating fragment with arguments
    public static ScreenSlidePageFragment newInstance(int lvl) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("lvl", lvl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLevelIndex = getArguments().getInt("lvl");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_level_selection, container, false);

        LevelLoader levelLoader = new LevelLoader(getActivity().getApplicationContext());
        int lvl = levelLoader.getLevelNumbers().get(mLevelIndex);
        findOrCreateLevelRecord(lvl);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        TextView lvlText = (TextView) rootView.findViewById(R.id.levelNumber);
        lvlText.setId(lvl);
        lvlText.setText(String.valueOf(lvl));
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font));
        lvlText.setTypeface(typeface);
        lvlText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 200);
        lvlText.setWidth(width);
        lvlText.setHeight(height);
        lvlText.setGravity(Gravity.CENTER);
        lvlText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectLevel(view);
            }
        });

        setLevelTextColor(lvlText);

        return rootView;
    }

    private void selectLevel(View view) {
        Intent intent = new Intent(getActivity(), LevelDetailActivity.class);
        intent.putExtra(LevelDetailActivity.LEVEL_ID, view.getId());
        startActivity(intent);
    }

    private void findOrCreateLevelRecord(int lvl) {
        List<LevelRecord> records = LevelRecord.find(LevelRecord.class, "level_nr = ?", Integer.toString(lvl));
        if (records.size() > 0) {
            mLevelRecord = records.get(0);
        } else {
            LevelRecord record = new LevelRecord(lvl, 0, 0, 0, 0);
            record.save();
            mLevelRecord = record;
        }
    }

    private void setLevelTextColor(TextView lvlText) {
        if (mLevelRecord.hasWon()) {
            lvlText.setTextColor(Color.GREEN);
        }
        else if (mLevelRecord.hasPlayed() && !mLevelRecord.hasWon()) {
            lvlText.setTextColor(Color.RED);
        }
    }
}
