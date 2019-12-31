package tz.app.sample.catfacts.presentation.progress;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class ProgressManager {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    @Nullable
    private AlertDialog progressDialog;

    private String lastMessage;

    private boolean wasShowing;

    public ProgressManager() {

    }

    public void attach(final Context context) {
        logger.debug(TAG, "attach, wasShowing = " + wasShowing);

        final ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(64, 0, 64, 0);

        progressDialog = new AlertDialog.Builder(context)
                .setView(progressBar)
                .setCancelable(false)
                .create();

        if (wasShowing) {
            showLoading(lastMessage);
        }
    }

    public void detach(final boolean _saveShowingState) {
        logger.debug(TAG, "attach, saveShowingState = " + _saveShowingState);

        final AlertDialog dialog = progressDialog;

        if (dialog == null) return;

        final boolean isShowing = dialog.isShowing();

        if (_saveShowingState) {
            wasShowing = isShowing;
        } else {
            wasShowing = false;
            lastMessage = null;
        }

        dismissProgress();

        progressDialog = null;

    }

    public final void showLoading(final String _msg) {
        logger.debug(TAG, "showLoading, msg = " + _msg);

        lastMessage = _msg;

        dismissProgress();

        final AlertDialog dialog = progressDialog;
        if (dialog == null) return;

        dialog.setTitle(_msg);
        dialog.show();
    }

    public final void showLoading() {
        showLoading("Loading...");
    }

    public final void hideLoading() {
        logger.debug(TAG, "hideLoading");
        dismissProgress();
    }

    private void dismissProgress() {
        final AlertDialog dialog = progressDialog;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
