package ch.sebastianhaeni.edgewars.graphics.drawables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeMap;

public class RenderQueue implements Iterable<Drawable> {
    private final TreeMap<Integer, ArrayList<Drawable>> mBuckets = new TreeMap<>();

    public void add(Drawable drawable, int layer) {
        synchronized (mBuckets) {
            if (!mBuckets.containsKey(layer)) {
                mBuckets.put(layer, new ArrayList<Drawable>());
            }
            mBuckets.get(layer).add(drawable);
        }
    }

    public synchronized void remove(Drawable drawable) {
        synchronized (mBuckets) {
            mBuckets.get(drawable.getLayer()).remove(drawable);
        }
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
            synchronized (mBuckets) {
                mLayers = mBuckets.navigableKeySet();
                mCurrentLayer = mLayers.first();
            }
        }

        @Override
        public boolean hasNext() {
            synchronized (mBuckets) {
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

                return mBuckets.get(layer).size() > 0;
            }
        }

        @Override
        public Drawable next() {
            synchronized (mBuckets) {
                if (mCurrentElement >= mBuckets.get(mCurrentLayer).size()) {
                    mCurrentLayer = mLayers.higher(mCurrentLayer);
                    mCurrentElement = 0;
                }

                Drawable next = mBuckets.get(mCurrentLayer).get(mCurrentElement);
                mCurrentElement++;

                return next;
            }
        }

        @Override
        public void remove() {
            synchronized (mBuckets) {
                mBuckets.get(mCurrentLayer).remove(mCurrentElement);
            }
        }
    }
}
