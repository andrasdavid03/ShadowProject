package shadow.platformer.ecs.systems.physics;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import shadow.platformer.ecs.components.HitboxComponent;
import shadow.platformer.ecs.components.JumpStatsComponent;
import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.System;
import shadow.platformer.services.tiles.Tile;

public class MovementSystem implements System {

    private final TilemapComponent tilemap;
    private final Rectangle tmpHitbox = new Rectangle();

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
                // Calculate proposed new position
                float newX = pos.x + vel.vx * delta;
                float newY = pos.y + vel.vy * delta;

                // Reuse tmpHitbox for collision
                tmpHitbox.set(hitbox.hitbox);

                // Move along X and Y separately
                moveAlongX(newX, pos.y, hitbox, vel);
                moveAlongY(pos.x, newY, hitbox, vel, jumpStats);

                // Update entity position from hitbox 
                pos.x = hitbox.hitbox.x;
                pos.y = hitbox.hitbox.y;
            }
        }
    }

    private void moveAlongX(float newX, float posY, HitboxComponent hitbox, VelocityComponent vel) {
        tmpHitbox.setPosition(newX, posY);

        int startX = Math.max(0, (int)Math.floor(tmpHitbox.x / tilemap.tileSize));
        int endX   = Math.min(tilemap.width - 1, (int)Math.ceil((tmpHitbox.x + tmpHitbox.width) / tilemap.tileSize) - 1);
        int startY = Math.max(0, (int)Math.floor(tmpHitbox.y / tilemap.tileSize));
        int endY   = Math.min(tilemap.height - 1, (int)Math.ceil((tmpHitbox.y + tmpHitbox.height) / tilemap.tileSize) - 1);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Tile tile = tilemap.tiles[y][x];
                if (tile.id == 0) continue;

                if (tmpHitbox.overlaps(tile.bounds)) {
                    if (vel.vx > 0) tmpHitbox.x = tile.bounds.x - tmpHitbox.width;
                    else if (vel.vx < 0) tmpHitbox.x = tile.bounds.x + tile.bounds.width;

                    vel.vx = 0f;
                }
            }
        }

        hitbox.hitbox.x = tmpHitbox.x;
    }

    private void moveAlongY(float posX, float newY, HitboxComponent hitbox, VelocityComponent vel, JumpStatsComponent jumpStats) {
        tmpHitbox.setPosition(posX, newY);

        int startX = Math.max(0, (int)Math.floor(tmpHitbox.x / tilemap.tileSize));
        int endX   = Math.min(tilemap.width - 1, (int)Math.ceil((tmpHitbox.x + tmpHitbox.width) / tilemap.tileSize) - 1);
        int startY = Math.max(0, (int)Math.floor(tmpHitbox.y / tilemap.tileSize));
        int endY   = Math.min(tilemap.height - 1, (int)Math.ceil((tmpHitbox.y + tmpHitbox.height) / tilemap.tileSize) - 1);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Tile tile = tilemap.tiles[y][x];
                if (tile.id == 0) continue;

                if (tmpHitbox.overlaps(tile.bounds)) {
                    if (vel.vy > 0) tmpHitbox.y = tile.bounds.y - tmpHitbox.height;
                    else if (vel.vy < 0) {
                        tmpHitbox.y = tile.bounds.y + tile.bounds.height;
                        jumpStats.jumpsLeft = jumpStats.maxJumps; // reset jumps
                    }

                    vel.vy = 0f;
                }
            }
        }

        hitbox.hitbox.y = tmpHitbox.y;
    }
}
