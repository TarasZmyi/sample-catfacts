package tz.app.sample.catfacts.utils.img;

import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.AnyThread;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

// TODO: add LRU disk cache besides mem cache.
final class ImageCache {

    /**
     * In memory LRU cache;
     */
    @GuardedBy("memCache")
    private final LruCache<String, Bitmap> memCache;

    @UiThread
    ImageCache() {
        // Get max available VM memory, exceeding this amount will throw an OOM exception.
        // Stored in kilobytes as LruCache takes an int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final String key, final Bitmap bmp) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bmp.getByteCount() / 1024;
            }
        };

    }

    @AnyThread
    final void keepBitmap(final String key, final Bitmap bmp) {
        synchronized (memCache) {
            final Bitmap cached = memCache.get(key);
            if (cached == null || cached.isRecycled()) {
                memCache.put(key, bmp);
            }
        }
    }

    @Nullable
    @AnyThread
    final Bitmap findBitmap(final String key) {
        synchronized (memCache) {
            final Bitmap cached = memCache.get(key);
            return (cached == null || cached.isRecycled()) ? null : cached;
        }
    }

}
