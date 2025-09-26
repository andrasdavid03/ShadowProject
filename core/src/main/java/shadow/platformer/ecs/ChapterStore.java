package shadow.platformer.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ChapterStore {
    private static final Map<String, Chapter> store = new HashMap<>();

    public static Chapter getOrCreate(String levelId, Supplier<Chapter> creator) {
        return store.computeIfAbsent(levelId, k -> creator.get());
    }

    public static Chapter get(String levelId) {
        return store.get(levelId);
    }

    public static void remove(String levelId, boolean dispose) {
        Chapter w = store.remove(levelId);
        if (w != null && dispose) w.dispose();
    }

    // for debugging or resetting
    public static void clearAll(boolean dispose) {
        if (dispose) store.values().forEach(Chapter::dispose);
        store.clear();
    }
}
