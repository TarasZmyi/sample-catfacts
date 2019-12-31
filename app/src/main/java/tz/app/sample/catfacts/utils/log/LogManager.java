package tz.app.sample.catfacts.utils.log;

import androidx.annotation.NonNull;

import tz.app.sample.catfacts.BuildConfig;

public final class LogManager {

    private static final ILogger logger = BuildConfig.DEBUG ? new DebugLogger() : new ReleaseLogger();

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
