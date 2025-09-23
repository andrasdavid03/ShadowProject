package shadow.platformer.ecs.systems;

import java.util.List;
import java.util.Map;

import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.TileType;

public class TileCollisionSystem implements System {
    private final Map<Integer, TileType> tileTypes;

    public TileCollisionSystem(Map<Integer, TileType> tileTypes) {
        this.tileTypes = tileTypes;
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        for (Entity e : entities) {
            if (!e.hasComponent(TransformComponent.class) || !e.hasComponent(VelocityComponent.class)) 
                continue;

            TransformComponent t = e.getComponent(TransformComponent.class);
            VelocityComponent v = e.getComponent(VelocityComponent.class);

            // Predict new position
            float newX = t.x + v.vx * delta;
            float newY = t.y + v.vy * delta;

            // Check collisions with all tilemaps
            for (Entity mapEntity : entities) {
                if (!mapEntity.hasComponent(TilemapComponent.class)) continue;
                TilemapComponent map = mapEntity.getComponent(TilemapComponent.class);

                // Bounding box corners
                int leftTile   = (int)(newX / map.tileSize);
                int rightTile  = (int)((newX + t.width) / map.tileSize);
                int bottomTile = (int)(newY / map.tileSize);
                int topTile    = (int)((newY + t.height) / map.tileSize);

                boolean collided = false;

                for (int ty = bottomTile; ty <= topTile; ty++) {
                    for (int tx = leftTile; tx <= rightTile; tx++) {
                        if (tx < 0 || ty < 0 || tx >= map.width || ty >= map.height) continue;
                        int tileId = map.tiles[ty][tx];
                        if (tileId != 0 && tileTypes.get(tileId).solid) {
                            collided = true;
                        }
                    }
                }

                if (!collided) {
                    t.x = newX;
                    t.y = newY;
                } else {
                    // stop velocity on collision
                    v.vx = 0;
                    v.vy = 0;
                }
            }
        }
    }
}

