package tzpace.app.catfactssample.utils.log;

import androidx.annotation.NonNull;

import tzpace.app.catfactssample.BuildConfig;

public final class LogManager {

    private static final ILogger logger = BuildConfig.DEBUG ? new LoggerDebugImpl() : new LoggerReleaseImpl();

    private LogManager() {
        /* prevent instantiating */
    }

    @NonNull
    public static ILogger getLogger() {
        return logger;
    }

    @NonNull
    public static String logObjCreation(final @NonNull Object _obj) {
        final String tag = createTag(_obj);
        logger.debug(tag, "Constructor");
        return tag;
    }

    @NonNull
    private static String createTag(final @NonNull Object _obj) {
        return _obj.getClass().getSimpleName() + "@" + Integer.toHexString(_obj.hashCode());
    }
}
