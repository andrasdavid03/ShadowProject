package shadow.platformer.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class WorldStore {
    private static final Map<String, World> store = new HashMap<>();

    public static World getOrCreate(String levelId, Supplier<World> creator) {
        return store.computeIfAbsent(levelId, k -> creator.get());
    }

    public static World get(String levelId) {
        return store.get(levelId);
    }

    public static void remove(String levelId, boolean dispose) {
        World w = store.remove(levelId);
        if (w != null && dispose) w.dispose();
    }

    // for debugging or resetting
    public static void clearAll(boolean dispose) {
        if (dispose) store.values().forEach(World::dispose);
        store.clear();
    }
}
