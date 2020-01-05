package tz.app.sample.catfacts.presentation.main.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import tz.app.sample.catfacts.R;
import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;
import tz.app.sample.catfacts.utils.img.ImageLoader;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class CatItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final FastOutLinearInInterpolator interpolator = new FastOutLinearInInterpolator();

    private TextView tvCatFactText;
    private ImageView imCatPicture;
    private ImageButton ibShareButton;

    @Nullable
    private ICatItemClickListener catItemClickListener;

    @Nullable
    private CatImg catImg;

    @Nullable
    private CatFact catFact;

    CatItemViewHolder(final View itemView) {
        super(itemView);
        imCatPicture = itemView.findViewById(R.id.id_im_ava_item_cat_fact);
        ibShareButton = itemView.findViewById(R.id.id_im_share_item_cat_fact);
        tvCatFactText = itemView.findViewById(R.id.id_tv_fact_txt_item_cat_fact);
    }

    final void bindData(final @NonNull CatItemDataHolder _catItemDataHolder) {
        logger.debug(TAG, "bindData");

        catImg = _catItemDataHolder.catImg;
        catFact = _catItemDataHolder.catFact;

        catItemClickListener = _catItemDataHolder.getCatItemClickListener();

        if (catFact != null) {
            final String text = catFact.factTxt;
            tvCatFactText.setText(text);
        }

    }

    final void attached() {
        final CatImg _catImg = catImg;
        if (_catImg != null) {
            ImageLoader.load(_catImg.imgUrl, imCatPicture);
        } else {
            // TODO: show error placeholder for image view, etc
            imCatPicture.setImageBitmap(null);
        }

        tvCatFactText.setTranslationY(0);
        ibShareButton.setOnClickListener(this);
    }

    final void detached() {
        imCatPicture.setImageBitmap(null);
        ibShareButton.setOnClickListener(null);

        final CatImg _catImg = catImg;
        if (_catImg != null) {
            ImageLoader.cancel(_catImg.imgUrl, imCatPicture);
        }

    }

    final void unbindData() {
        logger.debug(TAG, "unbindData");

        // todo: clear image view, etc

        catItemClickListener = null;
        catFact = null;
    }

    @Override
    public final void onClick(final View v) {
        switch (v.getId()) {
            case R.id.id_im_share_item_cat_fact:
                logger.debug(TAG, "onClick, Share");
                final ICatItemClickListener clickListener = catItemClickListener;
                if (clickListener != null) {
                    clickListener.onCatItemClickedShare(catFact);
                }
                break;
            default:
                logger.error(TAG, "onClick, NOT PROCESSED", null);
                break;
        }
    }

    public final void updateScrollOffset(float scrollOffset, int itemHeightPx) {
        // Offset TextView with a factor [-1.0..1.0] of the total item height

        float offsetFactor = (scrollOffset % itemHeightPx) / itemHeightPx;
        float field = coerceInOffset(-offsetFactor);
        float direction = field < 0 ? -1f : 1f;

        float interpolatedValue = interpolator.getInterpolation(Math.abs(field));
        float translationY = direction * interpolatedValue * itemHeightPx;

        tvCatFactText.setTranslationY(translationY);
    }

    public final void resetScrollOffset() {
        tvCatFactText.setTranslationY(0);
    }

    /**
     * Limits offset value by min/max range
     */
    private float coerceInOffset(float offset) {
        final float minRange = -1f;
        final float maxRange = 1f;

        if (minRange > offset) {
            return minRange;
        } else if (offset > maxRange) {
            return maxRange;
        } else {
            return offset;
        }
    }

}
