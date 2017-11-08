package tzpace.app.catfactssample;

import android.content.Context;

import tzpace.app.catfactssample.communication.CommunicationModuleImpl;
import tzpace.app.catfactssample.communication.ICommunicationModule;
import tzpace.app.catfactssample.communication.service.ApiHelper;
import tzpace.app.catfactssample.domain.catdata.CatDataModuleImpl;
import tzpace.app.catfactssample.domain.catdata.ICatDataModule;
import tzpace.app.catfactssample.domain.runtime.IRuntimeModule;
import tzpace.app.catfactssample.domain.runtime.RuntimeModuleImpl;
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

    private ICatDataModule catDataModule;

    private IRuntimeModule runtimeModule;

    final void init(final Context _appContext) {
        logger.debug(TAG, "init | appContext = " + _appContext);

        runtimeModule = new RuntimeModuleImpl(_appContext);

        final ICommunicationModule communicationModule = new CommunicationModuleImpl(new ApiHelper());
        catDataModule = new CatDataModuleImpl(communicationModule.getCatFactService(), communicationModule.getCatImgService());
    }

    public ICatDataModule getCatDataModule() {
        return catDataModule;
    }

    public IRuntimeModule getRuntimeModule() {
        return runtimeModule;
    }

    public static ObjectGraph getInstance() {
        return GRAPH;
    }

    public interface Provider {

        ObjectGraph getObjectGraph();

    }
}
