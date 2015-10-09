package ch.bfh.edgewars.ui;

import android.content.Context;
import android.view.MotionEvent;

import ch.bfh.edgewars.graphics.GameRenderer;
import ch.bfh.edgewars.logic.GameState;
import ch.bfh.edgewars.logic.entities.board.node.Node;
import ch.bfh.edgewars.logic.entities.board.node.state.NeutralState;
import ch.bfh.edgewars.logic.entities.board.node.state.OwnedState;
import ch.bfh.edgewars.ui.dialogs.NeutralNodeDialog;
import ch.bfh.edgewars.ui.dialogs.OpponentNodeDialog;
import ch.bfh.edgewars.ui.dialogs.OwnedNodeDialog;

public class GameController {

    private final GameState mGameState;
    private final GameRenderer mRenderer;
    private final Context mContext;
    private float mPreviousX;
    private float mPreviousY;
    private float mStartX;
    private float mStartY;

    public GameController(Context context, GameRenderer renderer, GameState gameState) {
        mContext = context;
        mGameState = gameState;
        mRenderer = renderer;
    }

    public void onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls.

        float x = e.getX();
        float y = e.getY();

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

    private void clickNode(float x, float y) {
        // center x and y to middle point of screen
        x = x - (mRenderer.getWidth() * .5f);
        y = y - (mRenderer.getHeight() * .5f);

        // get camera position
        float cameraX = mGameState.getCamera().getScreenX() * .5f;
        float cameraY = mGameState.getCamera().getScreenY() * .5f;

        // determine size of a node
        float nodeSize = 90; // TODO calculate this
        int scale = 141; // TODO calculate this

        // search through nodes if one lies at that coordinate
        for (Node node : mGameState.getBoard().getNodes()) {
            if (Math.abs((node.getPosition().getX() * scale) + cameraX - x) < nodeSize
                    && Math.abs((node.getPosition().getY() * scale) + cameraY - y) < nodeSize) {
                showNodeDialog(node);
                break;
            }
        }
    }

    /**
     * Shows the appropriate dialog of a node. These are 'neutral', 'owned' or 'opponent'.
     *
     * @param node
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
