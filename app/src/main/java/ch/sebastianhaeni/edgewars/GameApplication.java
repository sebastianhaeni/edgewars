package ch.sebastianhaeni.edgewars;

import android.app.Application;
import android.content.Context;

public class GameApplication extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        GameApplication.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return GameApplication.mContext;
    }
}
