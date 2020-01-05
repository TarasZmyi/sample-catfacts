package tz.app.sample.catfacts.utils.img;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.GuardedBy;
import androidx.annotation.UiThread;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Image download util;
 */
public final class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static final ExecutorService io = Executors.newFixedThreadPool(2);
    private static final ExecutorService computation = Executors.newFixedThreadPool(2);

    /**
     * ImageView instance with URL mapping,
     * Used for validating if target's url keeps same during loading process and making it relevant;
     * <p>
     * key is ImageView.hashCode();
     * value is String url;
     */
    @GuardedBy("targetingMap")
    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, String> targetingMap = Collections.synchronizedMap(new HashMap<>());

    private static volatile Handler ui;
    private static volatile ImageCache cache;
    private static volatile OkHttpClient client;

    @UiThread
    public static void init(final Context appContext) {
        ui = new Handler(appContext.getMainLooper());
        cache = new ImageCache();
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @UiThread
    public static void load(final String url, final ImageView img) {
        if (img == null) {
            return;
        }

        final Target target = prepareTarget(url, img);

        computation.execute(() -> {
            final Bitmap cachedBmp = cache.findBitmap(url);
            if (cachedBmp != null) {
                setImage(url, target.get(), cachedBmp);
            } else {
                io.execute(() -> {
                    if (validateTarget(url, target)) {

                        final Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();

                        // try (final InputStream in = new URL(url).openStream()) {
                        try (final Response response = client.newCall(request).execute()) {
                            if (validateTarget(url, target)) {
                                if (response.isSuccessful()) {
                                    final ResponseBody body = response.body();
                                    if (body != null) {
                                        final InputStream in = body.byteStream();

                                        //TODO: add decode android.graphics.BitmapFactory.Options;
                                        final Bitmap loadedBmp = BitmapFactory.decodeStream(in);

                                        setImage(url, target.get(), loadedBmp);
                                        cache.keepBitmap(url, loadedBmp);
                                    }
                                }
                            }
                        } catch (final IOException e) {
                            Log.e(TAG, "IOException", e);
                        } finally {
                            Log.d(TAG, "done");
                        }
                    }
                });
            }
        });

    }

    public static void cancel(final String url, final ImageView img) {
        synchronized (targetingMap) {
            if (validateImageView(url, img)) {
                targetingMap.remove(img.hashCode());
            }
        }
    }

    private static void setImage(final String url, final ImageView img, final Bitmap bmp) {
        Log.d(TAG, "setImage, url = " + url);
        ui.post(() -> {
            synchronized (targetingMap) {
                if (validateImageView(url, img)) {
                    img.setImageBitmap(bmp);
                    targetingMap.remove(img.hashCode());
                }
            }
        });
    }

    private static Target prepareTarget(final String url, final ImageView img) {
        synchronized (targetingMap) {
            targetingMap.put(img.hashCode(), url);
        }
        return new Target(img);
    }

    private static boolean validateTarget(final String url, final Target target) {
        synchronized (targetingMap) {
            return validateImageView(url, target.get());
        }
    }

    @GuardedBy("targetingMap")
    private static boolean validateImageView(final String url, final ImageView img) {
        return img != null && Objects.equals(targetingMap.get(img.hashCode()), url);
    }

    private static final class Target {

        final WeakReference<ImageView> target;

        Target(final ImageView img) {
            target = new WeakReference<>(img);
        }

        final ImageView get() {
            return target.get();
        }

    }

}

