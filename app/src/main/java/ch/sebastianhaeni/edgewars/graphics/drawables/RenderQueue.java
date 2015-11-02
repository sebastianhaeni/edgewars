package ch.sebastianhaeni.edgewars.graphics.drawables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeMap;

public class RenderQueue implements Iterable<Drawable> {
    private final TreeMap<Integer, ArrayList<Drawable>> mBuckets = new TreeMap<>();

    public void add(Drawable drawable, int layer) {
        if (!mBuckets.containsKey(layer)) {
            mBuckets.put(layer, new ArrayList<Drawable>());
        }
        mBuckets.get(layer).add(drawable);
    }

    public synchronized void remove(Drawable drawable) {
        mBuckets.get(drawable.getLayer()).remove(drawable);
    }

    @Override
    public synchronized DrawableIterator iterator() {
        return new DrawableIterator();
    }

    class DrawableIterator implements Iterator<Drawable> {

        private final NavigableSet<Integer> mLayers;
        private int mCurrentLayer;
        private int mCurrentElement = 0;

        public DrawableIterator() {
            mLayers = mBuckets.navigableKeySet();
            if (mLayers.size() <= 0) {
                return;
            }
            mCurrentLayer = mLayers.first();

        }

        @Override
        public boolean hasNext() {
            if (mBuckets.size() <= 0) {
                return false;
            }

            if (mCurrentElement < mBuckets.get(mCurrentLayer).size()) {
                return true;
            }

            if (mCurrentLayer == mLayers.last()) {
                return false;
            }

            int layer = mCurrentLayer;
            while (mLayers.higher(layer) != null) {
                layer = mLayers.higher(layer);
                if (mBuckets.get(layer).size() > 0) {
                    return true;
                }
            }

            return mCurrentLayer != layer && mBuckets.get(layer).size() > 0;
        }

        @Override
        public Drawable next() {
            int lastLayer = mCurrentLayer;
            int lastElement = mCurrentElement - 1;
            if (mCurrentElement >= mBuckets.get(mCurrentLayer).size()) {
                mCurrentElement = 0;
                mCurrentLayer = mLayers.higher(mCurrentLayer);

                while (mBuckets.get(mCurrentLayer).size() <= 0) {
                    mCurrentLayer = mLayers.higher(mCurrentLayer);
                }

            }

            // preventing exception when the queue has changed in the meantime
            if (mBuckets.get(mCurrentLayer).size() <= 0) {
                return mBuckets.get(lastLayer).get(lastElement);
            }

            Drawable next = mBuckets.get(mCurrentLayer).get(mCurrentElement);
            mCurrentElement++;

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
