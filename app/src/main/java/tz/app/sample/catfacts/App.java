package tz.app.sample.catfacts;

import android.app.Application;

import tz.app.sample.catfacts.utils.img.ImageLoader;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

/**
 * Extended Application class;
 *
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
        ImageLoader.init();
        ObjectGraph.init(getApplicationContext());
    }

}
