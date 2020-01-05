package tz.app.sample.catfacts.domain.runtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import tz.app.sample.catfacts.R;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class RuntimeModuleImpl implements IRuntimeModule {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final Context appContext;

    private int itemHeightWithDividerPx;

    public RuntimeModuleImpl(final Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public final void displayShareCatFact(final String factTxt) {
        logger.debug(TAG, "displayShareCatFact");

        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, factTxt);
        sendIntent.setType("text/plain");

        final String chooserLabel = appContext.getResources()
                .getText(R.string.txt_share_cat_fact)
                .toString();

        final Intent chooserIntent = Intent.createChooser(sendIntent, chooserLabel);
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        appContext.startActivity(chooserIntent);
    }

    @Override
    public final int getItemHeightWithDividerPx() {
        if (itemHeightWithDividerPx == 0) {
            final Resources res = appContext.getResources();
            final int itemHeightPx = res.getDimensionPixelSize(R.dimen.dimen_list_item_height);
            final int dividerHeightPx = res.getDimensionPixelSize(R.dimen.dimen_1dp);

            itemHeightWithDividerPx = itemHeightPx + dividerHeightPx;
        }
        logger.debug(TAG, "itemHeightWithDividerPx = " + itemHeightWithDividerPx);
        return itemHeightWithDividerPx;

    }
}
