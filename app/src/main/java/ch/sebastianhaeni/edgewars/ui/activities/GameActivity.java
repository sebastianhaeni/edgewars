package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ch.sebastianhaeni.edgewars.graphics.GameSurfaceView;

public class GameActivity extends Activity {

    private GameSurfaceView mGLView;
    private int mLevelNr;
    public static final String LEVEL_ID = "LEVEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLView = new GameSurfaceView(this);

        setContentView(mGLView);

        mLevelNr = 1;
        if (getIntent().getExtras() != null) {
            mLevelNr = getIntent().getExtras().getInt(LevelDetailActivity.LEVEL_ID);
        }
        mGLView.startLevel(mLevelNr);
    }

    @Override
    protected void onResume() {
        mGLView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mGLView.onPause();
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mGLView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void back(View view) {
        Intent intent = new Intent(GameActivity.this, LevelDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LEVEL_ID, mLevelNr);
        startActivity(intent);
        finish();
    }
}
