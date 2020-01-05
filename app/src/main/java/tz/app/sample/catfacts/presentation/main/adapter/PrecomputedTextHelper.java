package tz.app.sample.catfacts.presentation.main.adapter;

import android.widget.TextView;

import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO: impl TextView optimization
public final class PrecomputedTextHelper {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    // private PrecomputedTextCompat.Params paramsCatFactTxt = TextViewCompat.getTextMetricsParams(tvCatFactTxt);

    /** CatItemViewHolder.asyncSetText(tvCatFactTxt, paramsCatFactTxt, factTxt);*/
    private static void asyncSetText(final TextView textView,
                                     final PrecomputedTextCompat.Params params,
                                     final CharSequence factTxt) {

        final Reference<TextView> textViewRef = new WeakReference<>(textView);
        EXECUTOR.execute(() -> {

            final PrecomputedTextCompat precomputedText = PrecomputedTextCompat.create(factTxt, params);

            final TextView tv = textViewRef.get();
            if (tv == null) return;
            tv.post(() -> {
                final TextView ref = textViewRef.get();
                if (ref == null) return;

                TextViewCompat.setTextMetricsParams(ref, params);
                TextViewCompat.setPrecomputedText(ref, precomputedText);
            });
        });
    }

}
