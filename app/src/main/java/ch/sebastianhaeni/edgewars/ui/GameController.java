package ch.sebastianhaeni.edgewars.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import ch.sebastianhaeni.edgewars.EUnitType;
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
    private boolean mSelectingNode;
    private Node mSourceNode;
    private EUnitType mSendingUnitType;

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

    /**
     * Figures out if a node is clicked at that coordinate and what to do after.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void clickNode(float x, float y) {
        // center x and y to middle point of screen
        x = x - (mRenderer.getWidth() * .5f);
        y = y - (mRenderer.getHeight() * .5f);

        // get camera position
        float cameraX = mGameState.getCamera().getScreenX() * .5f;
        float cameraY = mGameState.getCamera().getScreenY() * .5f;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        // determine size of a node
        float nodeSize = 90; // TODO calculate this
        float scale = metrics.densityDpi * .25f;

        // search through nodes if one lies at that coordinate
        for (Node node : mGameState.getBoard().getNodes()) {
            if (Math.abs((node.getPosition().getX() * scale) + cameraX - x) < nodeSize
                    && Math.abs((node.getPosition().getY() * scale) + cameraY - y) < nodeSize) {

                if (mSelectingNode) {
                    mSelectingNode = false;
                    if (node.equals(mSourceNode)) {
                        return;
                    }

                    switch (mSendingUnitType) {
                        case MELEE:
                            mSourceNode.sendMeleeUnits(node);
                            break;
                        case SPRINTER:
                            mSourceNode.sendSprinterUnits(node);
                            break;
                        case TANK:
                            mSourceNode.sendTankUnits(node);
                            break;
                    }
                    return;
                }

                showNodeDialog(node);
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
            OwnedNodeDialog dialog = new OwnedNodeDialog(mContext, node, this);
            dialog.show();
            return;
        }

        OpponentNodeDialog dialog = new OpponentNodeDialog(mContext, node);
        dialog.show();
    }

    /**
     * Sends a melee along the way.
     *
     * @param node source node
     */
    public void askForMeleeTargetNode(Node node) {
        mSelectingNode = true;
        mSendingUnitType = EUnitType.MELEE;
        mSourceNode = node;
    }

    /**
     * Sends a tank along the way.
     *
     * @param node source node
     */
    public void askForTankTargetNode(Node node) {
        mSelectingNode = true;
        mSendingUnitType = EUnitType.TANK;
        mSourceNode = node;
    }

    /**
     * Sends a sprinter along the way.
     *
     * @param node source node
     */
    public void askForSprinterTargetNode(Node node) {
        mSelectingNode = true;
        mSendingUnitType = EUnitType.SPRINTER;
        mSourceNode = node;
    }
}
