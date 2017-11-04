package tzpace.app.catfactssample.utils.log;

import android.support.annotation.Nullable;
import android.util.Log;

final class LoggerDebugImpl implements ILogger {

    private final String LOG_TAG_PREFIX = "DebugLogger";

    LoggerDebugImpl() {
    }

    @Override
    public final void debug(final String _tag, final String _msg) {
        Log.d(LOG_TAG_PREFIX, _tag + " | " + _msg);
    }

    @Override
    public final void error(final String _tag, final String _msg, final @Nullable Throwable _tr) {
        if (_tr == null) {
            Log.e(LOG_TAG_PREFIX, _tag + " | " + _msg);
        } else {
            Log.e(LOG_TAG_PREFIX, _tag + " | " + _msg, _tr);
        }
    }

}
