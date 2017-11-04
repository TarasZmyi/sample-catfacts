package tzpace.app.catfactssample;

import android.content.Context;

import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public class ObjectGraph {

    private static final ObjectGraph GRAPH = new ObjectGraph();

    private final String TAG;

    private final ILogger logger;

    private ObjectGraph() {
        if (GRAPH != null) throw new Error("Only one ObjectGraph instance allowed.");
        TAG = LogManager.logObjCreation(this);
        logger = LogManager.getLogger();
    }

    final void init(final Context _appContext) {
        logger.debug(TAG, "init | appContext = " + _appContext);

        // TODO | init modules here.

    }

    public static ObjectGraph getInstance() {
        return GRAPH;
    }

    public interface Provider {

        ObjectGraph getObjectGraph();

    }
}
