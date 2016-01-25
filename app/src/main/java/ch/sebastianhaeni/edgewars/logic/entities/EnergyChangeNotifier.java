package ch.sebastianhaeni.edgewars.logic.entities;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Text;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * This class shows a small text below the energy that indicates changes in energy.
 */
class EnergyChangeNotifier extends Entity implements Observer {

    private static final int SHOW_TIME = 1000;
    private final HashMap<Integer, Text> mTexts = new HashMap<>();
    private int mRow = 0;
    private final HashMap<Integer, Long> mRowRefreshTime = new HashMap<>();

    @Override
    public void update(Observable observable, Object data) {
        if (!(data instanceof String)) {
            return;
        }

        if (mTexts.containsKey(mRow) && mTexts.get(mRow) != null) {
            mTexts.get(mRow).unregister();
        }
        mTexts.put(mRow, new Text(
                new Position(1.15f, 1f + (.45f * mRow)),
                Colors.ENERGY_DEDUCTION,
                "#######".substring(((String) data).length()) + data, // padding left
                10,
                true));
        mTexts.get(mRow).register();
        mRowRefreshTime.put(mRow, System.currentTimeMillis());

        mRow++;
        if (mRow >= 2) {
            mRow = 0;
        }
    }

    @Override
    public void update(long millis) {
        for (int row : mRowRefreshTime.keySet()) {
            if (mRowRefreshTime.get(row) < System.currentTimeMillis() - SHOW_TIME) {
                mTexts.get(row).unregister();
                if (row == 0) {
                    mRow = 0;
                }
            }
        }
    }

}
