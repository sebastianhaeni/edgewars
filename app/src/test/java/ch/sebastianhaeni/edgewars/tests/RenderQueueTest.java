package ch.sebastianhaeni.edgewars.tests;

import org.junit.Assert;
import org.junit.Test;

import ch.sebastianhaeni.edgewars.graphics.drawables.Drawable;
import ch.sebastianhaeni.edgewars.graphics.drawables.RenderQueue;
import ch.sebastianhaeni.edgewars.graphics.drawables.shapes.Polygon;
import ch.sebastianhaeni.edgewars.util.Colors;
import ch.sebastianhaeni.edgewars.util.Position;

/**
 * Unit tests for RenderQueue.
 */
public class RenderQueueTest {
    @Test
    public void TestRenderQueue() {
        RenderQueue q = new RenderQueue();
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 5, 4, 0, 1)); // layer 5
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 2, 4, 0, 1)); // layer 2
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 3, 4, 0, 1)); // layer 3
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 5, 4, 0, 1)); // layer 5
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 4, 4, 0, 1)); // layer 4
        q.add(new Polygon(new Position(0, 0), Colors.NODE_MINE, 1, 4, 0, 1)); // layer 1

        int layer = 1;
        int counter = 0;

        for (Drawable d : q) {
            Assert.assertTrue("Layer " + layer + " expected, " + d.getLayer() + " was actual", d.getLayer() == layer);
            if (layer < 5) {
                layer++;
            }
            counter++;
        }

        Assert.assertTrue("Size should be 6", counter == 6);

    }
}
