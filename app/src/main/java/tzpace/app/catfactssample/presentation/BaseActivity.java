package tzpace.app.catfactssample.presentation;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tzpace.app.catfactssample.ObjectGraph;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public abstract class BaseActivity extends AppCompatActivity implements ObjectGraph.Provider {

    protected final String TAG;
    protected final ILogger logger;

    private ObjectGraph objectGraph;

    public BaseActivity() {
        super();
        TAG = LogManager.logObjCreation(this);
        logger = LogManager.getLogger();
    }

    /**
     * Provide content view layout resource id.
     */
    @LayoutRes
    protected abstract int layoutResId();

    @Override
    protected void onCreate(final @Nullable Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        logger.debug(TAG, "onCreate | has savedInstanceState: " + (_savedInstanceState != null));

        objectGraph = ObjectGraph.getInstance();

        setContentView(layoutResId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.debug(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.debug(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger.debug(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.debug(TAG, "onDestroy | isFinishing = " + isFinishing());

        objectGraph = null;
    }

    @Override
    public final ObjectGraph getObjectGraph() {
        logger.debug(TAG, "getObjectGraph");
        return objectGraph;
    }
}
