package tz.app.sample.catfacts;

import android.content.Context;

import tz.app.sample.catfacts.communication.CommunicationModuleImpl;
import tz.app.sample.catfacts.communication.ICommunicationModule;
import tz.app.sample.catfacts.communication.service.ApiHelper;
import tz.app.sample.catfacts.domain.catdata.DataModuleImpl;
import tz.app.sample.catfacts.domain.catdata.IDataModule;
import tz.app.sample.catfacts.domain.runtime.IRuntimeModule;
import tz.app.sample.catfacts.domain.runtime.RuntimeModuleImpl;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

import static tz.app.sample.catfacts.communication.ICommunicationModule.CatFactService;
import static tz.app.sample.catfacts.communication.ICommunicationModule.CatImgService;

public class ObjectGraph {

    private final String TAG;

    private final ILogger logger;

    private IDataModule dataModule;
    private IRuntimeModule runtimeModule;

    private ObjectGraph() {
        final ObjectGraph objectGraph = getInstance();
        if (objectGraph != null) throw new Error("Only one ObjectGraph instance allowed.");
        TAG = LogManager.logObjCreation(this);
        logger = LogManager.getLogger();
    }

    static void init(final Context appContext) {
        final ObjectGraph objectGraph = getInstance();

        objectGraph.logger.debug(objectGraph.TAG, "init, appContext = " + appContext);

        objectGraph.runtimeModule = new RuntimeModuleImpl(appContext);

        final ApiHelper apiHelper = new ApiHelper();
        final ICommunicationModule communicationModule = new CommunicationModuleImpl(apiHelper);

        final CatImgService catImgService = communicationModule.getCatImgService();
        final CatFactService catFactService = communicationModule.getCatFactService();

        objectGraph.dataModule = new DataModuleImpl(catFactService, catImgService);
    }

    public static ObjectGraph getInstance() {
        return ObjectGraphHolder.INSTANCE;
    }

    public IDataModule getDataModule() {
        return dataModule;
    }

    public IRuntimeModule getRuntimeModule() {
        return runtimeModule;
    }

    public interface Provider {
        ObjectGraph getObjectGraph();
    }

    private static final class ObjectGraphHolder {
        private static final ObjectGraph INSTANCE = new ObjectGraph();
    }
}
