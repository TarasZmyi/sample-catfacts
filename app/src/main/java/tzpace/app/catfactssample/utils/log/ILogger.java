package tzpace.app.catfactssample.utils.log;

import androidx.annotation.Nullable;

public interface ILogger {

    void debug(String _tag, String _msg);

    void error(String _tag, String _msg, @Nullable Throwable _tr);

}
