package tz.app.sample.catfacts;

import android.app.Application;
import android.content.Context;

import tz.app.sample.catfacts.utils.img.ImageLoader;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

/**
 * Extended Application class;
 * <p>
 * Maintains global app state through ObjectGraph instance.
 */
public final class App extends Application {

    private final String TAG;
    private final ILogger logger;

    public App() {
        super();
        TAG = LogManager.logObjCreation(this);
        logger = LogManager.getLogger();
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        logger.debug(TAG, "onCreate");
        final Context appContext = getApplicationContext();
        ImageLoader.init(appContext);
        ObjectGraph.init(appContext);
    }

}
