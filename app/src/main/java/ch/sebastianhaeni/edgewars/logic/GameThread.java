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
    private final Object mPauseLock;

    private boolean mRunning;
    private boolean mPaused;
    private long mPausedTime;

    /**
     * Constructor
     */
    public GameThread() {
        mPauseLock = new Object();
    }

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
        long delta = getGameTime();
        while (mRunning) {
            // update game state
            Game.getInstance().update(getGameTime() - delta);

            delta = getGameTime();

            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (mPauseLock) {
                long pauseTimeStarted = System.currentTimeMillis();
                boolean paused = false;
                while (mPaused) {
                    paused = true;
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
                if (paused) {
                    mPausedTime += System.currentTimeMillis() - pauseTimeStarted;
                }
            }
        }
    }

    /**
     * Returns System.currentTimeMillis() - <time spent in pause>
     *
     * @return game time
     */
    public long getGameTime() {
        return System.currentTimeMillis() - mPausedTime;
    }

    /**
     * Call this on pause.
     */
    public void onPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    /**
     * Call this on resume.
     */
    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
}
