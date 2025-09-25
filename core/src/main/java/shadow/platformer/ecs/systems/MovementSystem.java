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
            HitboxComponent ownHitbox = e.getComponent(HitboxComponent.class);

            float moveX = vel.vx * deltaTime;
            float moveY = vel.vy * deltaTime;

            float newX = pos.x;
            float newY = pos.y;

            for (Entity other : entities) {
                if (other == e || !other.hasComponent(TilemapComponent.class)) continue;

                TilemapComponent tilemap = other.getComponent(TilemapComponent.class);

                // X axis movement
                newX += moveX;
                ownHitbox.hitbox.setPosition(newX, pos.y);

                int startX = (int)(ownHitbox.hitbox.x / tilemap.tileSize);
                int endX   = (int)((ownHitbox.hitbox.x + ownHitbox.hitbox.width) / tilemap.tileSize);
                int startY = (int)(ownHitbox.hitbox.y / tilemap.tileSize);
                int endY   = (int)((ownHitbox.hitbox.y + ownHitbox.hitbox.height) / tilemap.tileSize);

                startX = Math.max(0, startX);
                endX   = Math.min(tilemap.width - 1, endX);
                startY = Math.max(0, startY);
                endY   = Math.min(tilemap.height - 1, endY);

                boolean collided = false;
                for (int y = startY; y <= endY && !collided; y++) {
                    for (int x = startX; x <= endX; x++) {
                        if (tilemap.tiles[y][x].id == 0) continue;

                        Rectangle tileBounds = tilemap.tiles[y][x].bounds;
                        if (ownHitbox.hitbox.overlaps(tileBounds)) {
                            if (moveX > 0) newX = tileBounds.x - ownHitbox.hitbox.width;
                            else if (moveX < 0) newX = tileBounds.x + tileBounds.width;
                            collided = true;
                            break;
                        }
                    }
                }


                //Y axis movement (Jmp)
                newY += moveY;
                ownHitbox.hitbox.setPosition(newX, newY);

                startX = (int)(ownHitbox.hitbox.x / tilemap.tileSize);
                endX   = (int)((ownHitbox.hitbox.x + ownHitbox.hitbox.width) / tilemap.tileSize);
                startY = (int)(ownHitbox.hitbox.y / tilemap.tileSize);
                endY   = (int)((ownHitbox.hitbox.y + ownHitbox.hitbox.height) / tilemap.tileSize);

                startX = Math.max(0, startX);
                endX   = Math.min(tilemap.width - 1, endX);
                startY = Math.max(0, startY);
                endY   = Math.min(tilemap.height - 1, endY);

                collided = false;
                for (int y = startY; y <= endY && !collided; y++) {
                    for (int x = startX; x <= endX; x++) {
                        if (tilemap.tiles[y][x].id == 0) continue;

                        Rectangle tileBounds = tilemap.tiles[y][x].bounds;
                        if (ownHitbox.hitbox.overlaps(tileBounds)) {
                            if (moveY > 0) newY = tileBounds.y - ownHitbox.hitbox.height;
                            else if (moveY < 0) newY = tileBounds.y + tileBounds.height;
                            collided = true;

                            // Reset jump stats if landing
                            if (moveY < 0) {
                                jumpStats.jumpsLeft = jumpStats.maxJumps; // reset jumps on landing
                            }
                            vel.vy = 0; // stop vertical velocity on collision
                            
                            break;
                        }
                    }
                }
            }

            // finalize position
            ownHitbox.hitbox.setPosition(newX, newY);
            pos.x = newX;
            pos.y = newY;
        }
    }
}