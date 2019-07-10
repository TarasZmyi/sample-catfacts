package tzpace.app.catfactssample.presentation.main.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import tzpace.app.catfactssample.R;
import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;
import tzpace.app.catfactssample.domain.runtime.IRuntimeModule;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;
import tzpace.app.catfactssample.utils.spantext.ImageTextFlowSpannable;

class CatItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ImageTextFlowSpannable imageTextFlowSpannable;
    private final RequestOptions glideRequestOptions;

    private TextView tvCatFactTxt;
    private ImageView imAva;
    private ImageView imShare;

    @Nullable
    private ICatItemClickListener catItemClickListener;
    @Nullable
    private CatFact catFact;

    CatItemViewHolder(View itemView, final IRuntimeModule _runtimeModule) {
        super(itemView);

        imageTextFlowSpannable = _runtimeModule.getImageTextFlowSpannable();
        glideRequestOptions = _runtimeModule.getGlideRequestOptions();


        tvCatFactTxt = itemView.findViewById(R.id.id_tv_fact_txt_item_cat_fact);
        imAva = itemView.findViewById(R.id.id_im_ava_item_cat_fact);
        imShare = itemView.findViewById(R.id.id_im_share_item_cat_fact);
    }

    final void bindData(final @NonNull CatItemDataHolder _catItemDataHolder) {
        logger.debug(TAG, "bindData");

        catItemClickListener = _catItemDataHolder.getCatItemClickListener();
        catFact = _catItemDataHolder.catFact;

        imShare.setOnClickListener(this);

        if (catFact != null) {
            final SpannableString ss = new SpannableString(catFact.factTxt);
            ss.setSpan(imageTextFlowSpannable, 0, ss.length(), 0);
            tvCatFactTxt.setText(ss);
        }

        final CatImg catImg = _catItemDataHolder.catImg;

        if (catImg != null) {
            Glide.with(itemView.getContext())
                    .load(catImg.imgUrl)
                    .apply(glideRequestOptions)
                    .into(imAva);
        } else {
            Glide.with(itemView.getContext()).clear(imAva);
        }

    }

    final void unbindData() {
        logger.debug(TAG, "unbindData");
        imShare.setOnClickListener(null);
        Glide.with(itemView.getContext()).clear(imAva);

        catItemClickListener = null;
        catFact = null;
    }

    @Override
    public final void onClick(final View _v) {
        switch (_v.getId()) {
            case R.id.id_im_share_item_cat_fact:
                logger.debug(TAG, "onClick | share");

                if (catItemClickListener != null) {
                    catItemClickListener.onCatItemClickedShare(catFact);
                }
                break;
            default:
                logger.error(TAG, "onClick | NOT PROCESSED", null);
                break;
        }
    }
}
