package tz.app.sample.catfacts.utils.log;

import android.util.Log;

import androidx.annotation.Nullable;

final class DebugLogger implements ILogger {

    private final String LOG_TAG_PREFIX = "Logger";

    DebugLogger() {
    }

    @Override
    public final void debug(final String _tag, final String _msg) {
        Log.d(LOG_TAG_PREFIX, _tag + "\t" + _msg);
    }

    @Override
    public final void error(final String _tag, final String _msg, final @Nullable Throwable _tr) {
        if (_tr == null) {
            Log.e(LOG_TAG_PREFIX, _tag + "\t" + _msg);
        } else {
            Log.e(LOG_TAG_PREFIX, _tag + "\t" + _msg, _tr);
        }
    }

}
