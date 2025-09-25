package shadow.platformer.ecs.systems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import shadow.platformer.ecs.components.HitboxComponent;
import shadow.platformer.ecs.components.JumpStatsComponent;
import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.Tile;

public class MovementSystem implements System {

    private final TilemapComponent tilemap;   // single reference to level tilemap
    private final Rectangle tmpHitbox = new Rectangle(); // reusable rectangle for collisions

    public MovementSystem(TilemapComponent tilemap) {
        this.tilemap = tilemap;
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        for (Entity e : entities) {
            TransformComponent pos = e.getComponent(TransformComponent.class);
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            JumpStatsComponent jumpStats = e.getComponent(JumpStatsComponent.class);
            HitboxComponent hitbox = e.getComponent(HitboxComponent.class);
            PlayerControllable pc = e.getComponent(PlayerControllable.class);

            if (pos != null && vel != null && jumpStats != null && hitbox != null && pc != null) {
                float moveX = vel.vx * delta;
                float moveY = vel.vy * delta;

                float newX = pos.x + moveX;
                float newY = pos.y + moveY;

                // Reuse tmpHitbox for collision
                tmpHitbox.set(hitbox.hitbox);

                // Move along X axis
                moveAlongX(newX, pos.y, hitbox, vel);

                // Move along Y axis
                moveAlongY(pos.x, newY, hitbox, vel, jumpStats);

                // Apply final position
                pos.x = hitbox.hitbox.x;
                pos.y = hitbox.hitbox.y;
            }
        }
    }

    private void moveAlongX(float newX, float posY, HitboxComponent hitbox, VelocityComponent vel) {
        tmpHitbox.setPosition(newX, posY);

        int startX = Math.max(0, (int)(tmpHitbox.x / tilemap.tileSize));
        int endX = Math.min(tilemap.width - 1, (int)((tmpHitbox.x + tmpHitbox.width) / tilemap.tileSize));
        int startY = Math.max(0, (int)(tmpHitbox.y / tilemap.tileSize));
        int endY = Math.min(tilemap.height - 1, (int)((tmpHitbox.y + tmpHitbox.height) / tilemap.tileSize));

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Tile tile = tilemap.tiles[y][x];
                if (tile.id == 0) continue;

                if (tmpHitbox.overlaps(tile.bounds)) {
                    // Resolve X collision
                    if (vel.vx > 0) tmpHitbox.x = tile.bounds.x - tmpHitbox.width;
                    else if (vel.vx < 0) tmpHitbox.x = tile.bounds.x + tile.bounds.width;

                    vel.vx = 0;
                }
            }
        }

        hitbox.hitbox.x = tmpHitbox.x; // update hitbox position
    }

    private void moveAlongY(float posX, float newY, HitboxComponent hitbox, VelocityComponent vel, JumpStatsComponent jumpStats) {
        tmpHitbox.setPosition(posX, newY);

        int startX = Math.max(0, (int)(tmpHitbox.x / tilemap.tileSize));
        int endX = Math.min(tilemap.width - 1, (int)((tmpHitbox.x + tmpHitbox.width) / tilemap.tileSize));
        int startY = Math.max(0, (int)(tmpHitbox.y / tilemap.tileSize));
        int endY = Math.min(tilemap.height - 1, (int)((tmpHitbox.y + tmpHitbox.height) / tilemap.tileSize));

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Tile tile = tilemap.tiles[y][x];
                if (tile.id == 0) continue;

                if (tmpHitbox.overlaps(tile.bounds)) {
                    // Resolve Y collision
                    if (vel.vy > 0) tmpHitbox.y = tile.bounds.y - tmpHitbox.height;
                    else if (vel.vy < 0) {
                        tmpHitbox.y = tile.bounds.y + tile.bounds.height;
                        jumpStats.jumpsLeft = jumpStats.maxJumps; // reset jumps
                    }

                    vel.vy = 0;
                }
            }
        }

        hitbox.hitbox.y = tmpHitbox.y; // update hitbox position
    }
}