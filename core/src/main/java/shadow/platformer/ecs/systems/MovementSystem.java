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

public class MovementSystem implements System {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity e : entities) {
            if (!e.hasComponent(TransformComponent.class)
                || !e.hasComponent(VelocityComponent.class)
                || !e.hasComponent(PlayerControllable.class)
                || !e.hasComponent(HitboxComponent.class))
                continue;

            TransformComponent pos = e.getComponent(TransformComponent.class);
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            JumpStatsComponent jumpStats = e.getComponent(JumpStatsComponent.class);
            HitboxComponent hitbox = e.getComponent(HitboxComponent.class);

            float moveX = vel.vx * deltaTime;
            float moveY = vel.vy * deltaTime;

            float newX = pos.x;
            float newY = pos.y;

            for (Entity other : entities) {
                if (other == e || !other.hasComponent(TilemapComponent.class)) continue;

                TilemapComponent tilemap = other.getComponent(TilemapComponent.class);

                newX = moveAlongX(newX, pos.y, moveX, hitbox, tilemap);
                newY = moveAlongY(newY, newX, moveY, hitbox, tilemap, vel, jumpStats);
            }

            pos.x = newX;
            pos.y = newY;
        }
    }

    private float moveAlongX(
            float newX, float posY, float moveX,
            HitboxComponent hitbox, TilemapComponent tilemap) {

        newX += moveX;
        checkCollision(hitbox.hitbox, tilemap, newX, posY, moveX, 0, null, null);
        return hitbox.hitbox.x; // updated after resolution
    }

    private float moveAlongY(
            float newY, float posX, float moveY,
            HitboxComponent hitbox, TilemapComponent tilemap,
            VelocityComponent vel, JumpStatsComponent jumpStats) {

        newY += moveY;
        checkCollision(hitbox.hitbox, tilemap, posX, newY, 0, moveY, vel, jumpStats);
        return hitbox.hitbox.y;
    }

    private boolean checkCollision(
            Rectangle hitbox, TilemapComponent tilemap,
            float newX, float newY, float moveX, float moveY,
            VelocityComponent vel, JumpStatsComponent jumpStats) {

        hitbox.setPosition(newX, newY);

        int startX = Math.max(0, (int)(hitbox.x / tilemap.tileSize));
        int endX   = Math.min(tilemap.width - 1, (int)((hitbox.x + hitbox.width) / tilemap.tileSize));
        int startY = Math.max(0, (int)(hitbox.y / tilemap.tileSize));
        int endY   = Math.min(tilemap.height - 1, (int)((hitbox.y + hitbox.height) / tilemap.tileSize));

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                if (tilemap.tiles[y][x].id == 0) continue;

                Rectangle tileBounds = tilemap.tiles[y][x].bounds;
                if (hitbox.overlaps(tileBounds)) {
                    // X axis resolution
                    if (moveX > 0) newX = tileBounds.x - hitbox.width;
                    else if (moveX < 0) newX = tileBounds.x + tileBounds.width;

                    // Y axis resolution
                    if (moveY > 0) newY = tileBounds.y - hitbox.height;
                    else if (moveY < 0) {
                        newY = tileBounds.y + tileBounds.height;
                        jumpStats.jumpsLeft = jumpStats.maxJumps; // reset jumps
                    }

                    // Stop velocity if vertical collision
                    if (moveY != 0) vel.vy = 0;

                    hitbox.setPosition(newX, newY);
                    return true; // collided
                }
            }
        }
        return false;
    }
}