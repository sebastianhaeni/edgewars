package ch.bfh.edgewars.logic;

public class GameThread extends Thread {
    private final GameState mGameState;
    private boolean mRunning;

    public GameThread(GameState gameState) {
        mGameState = gameState;
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        long delta = System.currentTimeMillis();
        long sleep;
        while (mRunning) {
            // update game state
            delta = System.currentTimeMillis() - delta;

            mGameState.update(delta);
            try {
                sleep = 16 - delta;
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
