package shadow.platformer.services.tiles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TileRegistry {
    private final Map<Integer, TileType> tileTypes = new HashMap<>();

    public void register(int id, TileType type) {
        tileTypes.put(id, type);
    }

    public TileType get(int id) {
        return tileTypes.get(id);
    }

    public Map<Integer, TileType> getAll() {
        return Collections.unmodifiableMap(tileTypes);
    }
}
