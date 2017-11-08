package tzpace.app.catfactssample.domain.runtime;

import android.content.Context;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import tzpace.app.catfactssample.R;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;
import tzpace.app.catfactssample.utils.spantext.ImageTextFlowSpannable;

public final class RuntimeModuleImpl implements IRuntimeModule {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ImageTextFlowSpannable imageTextFlowSpannable;
    private final RequestOptions glideRequestOptions;

    public RuntimeModuleImpl(final Context _appContext) {

        imageTextFlowSpannable = new ImageTextFlowSpannable(3, _appContext.getResources().getDimensionPixelSize(R.dimen.margin_flow_text_item_cat_fact));

        glideRequestOptions = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .error(R.drawable.ic_error_outline)
                .placeholder(R.drawable.bg_rect_line_item_cat_fact);

    }

    @Override
    public ImageTextFlowSpannable getImageTextFlowSpannable() {
        logger.debug(TAG, "getImageTextFlowSpannable");
        return imageTextFlowSpannable;
    }

    @Override
    public RequestOptions getGlideRequestOptions() {
        logger.debug(TAG, "getGlideRequestOptions");
        return glideRequestOptions;
    }
}
