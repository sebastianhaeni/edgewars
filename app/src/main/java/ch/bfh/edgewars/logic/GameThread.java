package ch.bfh.edgewars.logic;

public class GameThread extends Thread {
    private boolean mRunning;

    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        long delta = System.currentTimeMillis();
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
