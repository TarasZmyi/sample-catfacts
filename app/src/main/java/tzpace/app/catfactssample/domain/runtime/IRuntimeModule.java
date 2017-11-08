package tzpace.app.catfactssample.domain.runtime;

import com.bumptech.glide.request.RequestOptions;

import tzpace.app.catfactssample.utils.spantext.ImageTextFlowSpannable;

public interface IRuntimeModule {

    ImageTextFlowSpannable getImageTextFlowSpannable();

    RequestOptions getGlideRequestOptions();

}
