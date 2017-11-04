package tzpace.app.catfactssample;

import android.app.Application;

import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

/**
 * The Application class implementation for maintaining global app state.
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
        ObjectGraph.getInstance().init(this);
    }

}
