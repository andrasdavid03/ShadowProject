package shadow.platformer.ecs.systems;

import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteCache;

import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.services.tiles.TileType;

public class TileRenderSystem implements System {
    private final TileRegistry tileRegistry;
    private final SpriteCache cache;
    private int cacheId;
    private final Camera camera;

    public TileRenderSystem(TileRegistry tileRegistry, int expectedTiles, Camera camera) {
        this.tileRegistry = tileRegistry;
        this.cache = new SpriteCache(expectedTiles, false);
        this.camera = camera;
    }

    public void buildCache(TilemapComponent tilemap) {
        cache.beginCache();

        for (int y = 0; y < tilemap.height; y++) {
            for (int x = 0; x < tilemap.width; x++) {
                int tileId = tilemap.tiles[y][x].id;
                if (tileId == 0) continue;

                TileType type = tileRegistry.get(tileId);

                cache.add(type.texture,
                        x * tilemap.tileSize,
                        y * tilemap.tileSize,
                        tilemap.tileSize,
                        tilemap.tileSize);
            }
        }

        cacheId = cache.endCache();
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        cache.setProjectionMatrix(camera.combined); // <- apply camera here too
        cache.begin();
        cache.draw(cacheId);
        cache.end();
    }
}
