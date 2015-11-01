package ch.sebastianhaeni.edgewars.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.ui.dialogs.NeutralNodeDialog;
import ch.sebastianhaeni.edgewars.ui.dialogs.OpponentNodeDialog;
import ch.sebastianhaeni.edgewars.ui.dialogs.OwnedNodeDialog;

/**
 * The game controller handles inputs from the user and delegates them to the according action.
 */
public class GameController {

    private final GameState mGameState;
    private final GameRenderer mRenderer;
    private final Context mContext;
    private float mPreviousX;
    private float mPreviousY;
    private float mStartX;
    private float mStartY;

    /**
     * Constructor
     *
     * @param context   app context
     * @param renderer  game renderer
     * @param gameState game state
     */
    public GameController(Context context, GameRenderer renderer, GameState gameState) {
        mContext = context;
        mGameState = gameState;
        mRenderer = renderer;
    }

    /**
     * Handles touch events on the OpenGL ES surface.
     *
     * @param e the motion event
     */
    public void onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls.

        float x = e.getRawX();
        float y = e.getRawY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                mGameState.getCamera().takeCamera();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mGameState.getCamera().moveCamera(dx, dy);
                break;
            case MotionEvent.ACTION_UP:
                // detect single click (not moving the camera)
                if (e.getEventTime() - e.getDownTime() < 200
                        && Math.abs(x - mStartX) < 5
                        && Math.abs(y - mStartY) < 5) {
                    clickNode(x, y);
                }
                mGameState.getCamera().freeCamera();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
    }

    /**
     * Figures out if a node is clicked at that coordinate and what to do after.
     *
     * @param clickX x coordinate
     * @param clickY y coordinate
     */
    private void clickNode(float clickX, float clickY) {

        Log.i("debugging", "___________________________________________");
        Log.i("debugging", "clickX=" + clickX + "; clickY=" + clickY);

        // get camera position
        float cameraX = mGameState.getCamera().getScreenX() * (2f / 3f);
        float cameraY = mGameState.getCamera().getScreenY() * (2f / 3f);
        Log.i("debugging", "cameraX=" + cameraX + "; cameraY=" + cameraY);

        float nodeLengthX = mRenderer.getAndroidLengthX(0.5f);
        float nodeLengthY = mRenderer.getAndroidLengthY(0.5f);

        // + 1/3 imprecision tolerance
        nodeLengthX = nodeLengthX * 1.33f;
        nodeLengthY = nodeLengthY * 1.33f;
        Log.i("debugging", "nodeLengthX=" + nodeLengthX + "; nodeLengthY=" + nodeLengthY);


        // loop through nodes and test if one lies at the click coordinate
        for (Node node : mGameState.getBoard().getNodes()) {

            float nodeX = mRenderer.getAndroidCoordinateX(node.getPosition().getX());
            float nodeY = mRenderer.getAndroidCoordinateY(node.getPosition().getY());

            Log.i("debugging", "calcNodeX[i]=" + nodeX + "; calcNodeY[i]=" + nodeY);

            if (Math.abs(nodeX + cameraX - clickX) < nodeLengthX &&
                    Math.abs(nodeY + cameraY - clickY) < nodeLengthY) {
                showNodeDialog(node);

                float originalCameraX = mGameState.getCamera().getX();
                float originalCameraY = mGameState.getCamera().getY();

                Log.i("debugging", "cameraX=" + originalCameraX + "; cameraY=" + originalCameraY);
                Log.i("debugging", "nodeX=" + node.getPosition().getX() + "; nodeY=" + node.getPosition().getY());
                Log.i("debugging", "calcNodeX=" + nodeX + "; calcNodeY=" + nodeY);
                float calcClickX = clickX - originalCameraX;
                float calcClickY = clickY - originalCameraY;
                Log.i("debugging", "calcClickX=" + calcClickX + "; calcClickY=" + calcClickY);
                break;
            }
        }
    }

    /**
     * Shows the appropriate dialog of a node. These are 'neutral', 'owned' or 'opponent'.
     *
     * @param node of which the dialog should be opened
     */
    private void showNodeDialog(Node node) {
        if (node.getState() instanceof NeutralState) {
            NeutralNodeDialog dialog = new NeutralNodeDialog(mContext, node);
            dialog.show();
            return;
        }

        if (!(node.getState() instanceof OwnedState)) {
            return;
        }

        OwnedState state = (OwnedState) node.getState();

        if (state.getOwner().equals(mGameState.getHuman())) {
            OwnedNodeDialog dialog = new OwnedNodeDialog(mContext, node);
            dialog.show();
            return;
        }

        OpponentNodeDialog dialog = new OpponentNodeDialog(mContext, node);
        dialog.show();
    }
}
