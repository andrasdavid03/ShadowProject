package shadow.platformer.ecs.systems;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.services.tiles.TileType;

public class TileRenderSystem implements System {
    private final SpriteBatch batch;
    private final TileRegistry tileRegistry;

    public TileRenderSystem(SpriteBatch batch, TileRegistry tileRegistry) {
        this.batch = batch;
        this.tileRegistry = tileRegistry;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        batch.begin();

        for (Entity e : entities) {
            if (e.hasComponent(TilemapComponent.class)) {
                TilemapComponent tilemap = e.getComponent(TilemapComponent.class);

                for (int y = 0; y < tilemap.height; y++) {
                    for (int x = 0; x < tilemap.width; x++) {
                        int tileId = tilemap.tiles[y][x].id;

                        if (tileId == 0) continue; // Skip empty tiles

                        TileType type = tileRegistry.get(tileId);

                        batch.draw(type.texture,
                                x * tilemap.tileSize,
                                y * tilemap.tileSize,
                                tilemap.tileSize,
                                tilemap.tileSize);
                    }
                }
            }
        }

        batch.end();
    }
}