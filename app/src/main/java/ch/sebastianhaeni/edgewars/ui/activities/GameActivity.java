package ch.sebastianhaeni.edgewars.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ch.sebastianhaeni.edgewars.graphics.GameSurfaceView;
import ch.sebastianhaeni.edgewars.logic.GameState;

public class GameActivity extends Activity {

    private GameSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLView = new GameSurfaceView(this);

        if(savedInstanceState != null) {
            GameState state = (GameState) savedInstanceState.getSerializable("gameState");
            mGLView.setState(state);
//        System.out.println(state.getHuman().getEnergy());
        }

        setContentView(mGLView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int levelNr = 1;
        if (getIntent().getExtras() != null) {
            levelNr = getIntent().getExtras().getInt(LevelDetailActivity.LEVEL_ID);
        }

        mGLView.startLevel(levelNr);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("gameState", mGLView.getState());
    }
}
