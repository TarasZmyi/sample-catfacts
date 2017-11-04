package tzpace.app.catfactssample.utils.log;

import android.support.annotation.Nullable;

public interface ILogger {

    void debug(String _tag, String _msg);

    void error(String _tag, String _msg, @Nullable Throwable _tr);

}
