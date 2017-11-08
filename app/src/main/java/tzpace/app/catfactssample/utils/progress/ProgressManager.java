package tzpace.app.catfactssample.utils.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;

import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public final class ProgressManager {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    @Nullable
    private ProgressDialog progressDialog;

    private String lastMessage;

    private boolean wasShowing;

    public ProgressManager() {

    }

    public void attach(final Context context) {
        logger.debug(TAG, "attach | wasShowing = " + wasShowing);
        progressDialog = new ProgressDialog(context);
        if (wasShowing) {
            showLoading(lastMessage);
        }
    }

    public void detach(final boolean _saveShowingState) {
        logger.debug(TAG, "attach | saveShowingState = " + _saveShowingState);

        if (progressDialog == null) return;

        final boolean isShowing = progressDialog.isShowing();

        if (_saveShowingState) {
            wasShowing = isShowing;
        } else {
            wasShowing = false;
            lastMessage = null;
        }

        if (isShowing) {
            progressDialog.dismiss();
        }

        progressDialog = null;

    }

    public final void showLoading(final String _msg) {
        logger.debug(TAG, "showLoading | msg = " + _msg);

        lastMessage = _msg;

        if (progressDialog == null) return;

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog.setMessage(_msg);
        progressDialog.show();
    }

    public final void showLoading() {
        showLoading("Loading...");
    }

    public final void hideLoading() {
        logger.debug(TAG, "hideLoading");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
