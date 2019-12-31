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

    static void init(final Context _appContext) {
        final ObjectGraph objectGraph = getInstance();

        objectGraph.logger.debug(objectGraph.TAG, "init, appContext = " + _appContext);

        objectGraph.runtimeModule = new RuntimeModuleImpl(_appContext);

        final ICommunicationModule communicationModule = new CommunicationModuleImpl(new ApiHelper());
        objectGraph.dataModule = new DataModuleImpl(communicationModule.getCatFactService(), communicationModule.getCatImgService());
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
