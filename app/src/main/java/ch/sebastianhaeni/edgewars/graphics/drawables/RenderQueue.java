package ch.sebastianhaeni.edgewars.graphics.drawables;

import com.google.common.collect.Ordering;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Provides an interface to order the drawables to their drawing layer.
 */
public class RenderQueue implements Iterable<Drawable> {

    private final ConcurrentSkipListSet<Drawable> mDrawables = new ConcurrentSkipListSet<>(new Ordering<Drawable>() {
        @Override
        public int compare(Drawable left, Drawable right) {
            int diff = left.getLayer() - right.getLayer();
            if (diff != 0) {
                return diff;
            }
            return left.equals(right) ? 0 : left.hashCode() - right.hashCode();
        }
    });

    /**
     * @param drawable drawable to add
     */
    public void add(Drawable drawable) {
        mDrawables.add(drawable);
    }

    /**
     * @param drawable drawable to remove
     */
    public void remove(Drawable drawable) {
        mDrawables.remove(drawable);
    }

    @Override
    public Iterator<Drawable> iterator() {
        return mDrawables.iterator();
    }
}
