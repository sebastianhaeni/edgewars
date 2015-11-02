package ch.sebastianhaeni.edgewars.logic;

/**
 * The game update thread updates the game while it's running. This is done in a thread to not
 * block the main thread. All in all there are 3 threads:
 * <ol>
 * <li>Main thread (for the activity)</li>
 * <li>Game update thread</li>
 * <li>Graphics thread (controlled by OpenGL ES)</li>
 * </ol>
 */
public class GameThread extends Thread {
    private boolean mRunning;

    /**
     * Set the game to run or not to run. That's the question.
     *
     * @param running set to false to stop the game
     */
    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        long delta = System.currentTimeMillis();

        // initialize sound engine in this thread because we don't want to block the main thread
        SoundEngine.getInstance();

        while (mRunning) {
            // updateState game state
            delta = System.currentTimeMillis() - delta;

            Game.getInstance().update(delta);

            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
