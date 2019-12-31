package tz.app.sample.catfacts.presentation._base;

import java.util.HashMap;
import java.util.Map;

public class ModelCache {

    private static final Map<String, BaseModel<?>> modelCacheMap = new HashMap<>();

    private ModelCache() {
    }

    public static void storeModel(final String key, final BaseModel<?> model) {
        modelCacheMap.put(key, model);
    }

    public static void removeModel(final String key) {
        modelCacheMap.remove(key);
    }

    @SuppressWarnings("unchecked")
    public static <M extends BaseModel> M retrieveModel(final String key) {
        return (M) modelCacheMap.get(key);
    }


    public static boolean hasModel(final String key) {
        return modelCacheMap.containsKey(key);
    }
}
