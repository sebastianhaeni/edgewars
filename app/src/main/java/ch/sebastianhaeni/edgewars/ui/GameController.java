package ch.sebastianhaeni.edgewars.ui;

import android.content.Context;
import android.view.MotionEvent;

import java.util.ArrayList;

import ch.sebastianhaeni.edgewars.EUnitType;
import ch.sebastianhaeni.edgewars.graphics.GameRenderer;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Shape;
import ch.sebastianhaeni.edgewars.logic.Game;
import ch.sebastianhaeni.edgewars.logic.GameState;
import ch.sebastianhaeni.edgewars.logic.SoundEngine;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.Node;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.NeutralState;
import ch.sebastianhaeni.edgewars.logic.entities.board.node.state.OwnedState;
import ch.sebastianhaeni.edgewars.ui.dialogs.NeutralNodeDialog;
import ch.sebastianhaeni.edgewars.ui.dialogs.OpponentNodeDialog;
import ch.sebastianhaeni.edgewars.ui.dialogs.OwnedNodeDialog;
import ch.sebastianhaeni.edgewars.util.Colors;

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
    private final ArrayList<Shape> mCoronas = new ArrayList<>();

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
                    SoundEngine.getInstance().play(SoundEngine.Sounds.CLICK);
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
     * @param touchX x coordinate
     * @param touchY y coordinate
     */
    private void clickNode(float touchX, float touchY) {

        // get camera position and multiply it by factor 2/3 (why? I dunno..)
        float cameraX = mGameState.getCamera().getScreenX() * (2f / 3f);
        float cameraY = mGameState.getCamera().getScreenY() * (2f / 3f);

        // loop through all nodes and test if one is positioned at the coordinates of the user touch
        for (Node node : mGameState.getBoard().getNodes()) {
            // calculate node radius in pixels
            float nodeRadiusGL = node.getRadius();
            float nodeRadiusX = mRenderer.getAndroidLengthX(nodeRadiusGL);
            float nodeRadiusY = mRenderer.getAndroidLengthY(nodeRadiusGL);

            // add 33% user imprecision tolerance
            nodeRadiusX = nodeRadiusX * 1.33f;
            nodeRadiusY = nodeRadiusY * 1.33f;

            // convert node coordinates to Android coordinates
            float nodeX = mRenderer.getAndroidCoordinateX(node.getPosition().getX());
            float nodeY = mRenderer.getAndroidCoordinateY(node.getPosition().getY());

            if (!(Math.abs(nodeX + cameraX - touchX) < nodeRadiusX &&
                    Math.abs(nodeY + cameraY - touchY) < nodeRadiusY)) {
                continue;
            }

            if (mSelectingNode) {
                mSelectingNode = false;

                if (node.equals(mSourceNode)
                        || !Game.getInstance().getConnectedNodes(mSourceNode).contains(node)) {
                    showNodeDialog(node);
                    break;
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
                SoundEngine.getInstance().play(SoundEngine.Sounds.UNIT_SENT);
            } else {
                showNodeDialog(node);
            }
            break;
        }

        clearCoronas();
    }

    /**
     * Clears coronas off nodes.
     */
    private void clearCoronas() {
        for (Shape corona : mCoronas) {
            corona.destroy();
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
     * Asks the player the player for the units to be sent to.
     *
     * @param node source node
     * @param type type of unit
     */
    public void askPlayerForTargetNode(Node node, EUnitType type) {
        mSelectingNode = true;
        mSourceNode = node;
        mSendingUnitType = type;
        mCoronas.clear();

        for (Node neighbor : Game.getInstance().getConnectedNodes(node)) {
            Polygon corona = new Polygon(neighbor.getPosition(), Colors.CORONA, 1, 300, 0, .75f);
            corona.register();
            mCoronas.add(corona);
        }
    }

}
