package shadow.platformer.ecs.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import shadow.platformer.ecs.components.JumpStatsComponent;
import shadow.platformer.ecs.components.MovementStatsComponent;
import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.events.EventBus;
import shadow.platformer.events.eventTypes.SpacePressedEvent;

public class InputSystem implements System {
    private final EventBus bus;

    public InputSystem(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        for (Entity e : entities) {
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            MovementStatsComponent stats = e.getComponent(MovementStatsComponent.class);

            if (vel != null && stats != null && e.hasComponent(PlayerControllable.class)) {
                // Movement vectors
                float inputX = 0f;

                if (Gdx.input.isKeyPressed(Input.Keys.A)) inputX -= 1;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) inputX += 1;

                // Apply movement speed
                vel.vx = inputX * stats.maxSpeed;

                // Variable jump height
                JumpStatsComponent jumpStats = e.getComponent(JumpStatsComponent.class);
                if (jumpStats != null) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        if (!jumpStats.isJumping && jumpStats.jumpsLeft > 0) {
                            // Start jump
                            jumpStats.isJumping = true;
                            jumpStats.jumpsLeft -= 1;
                            vel.vy = jumpStats.jumpForce;
                            bus.publish(new SpacePressedEvent());
                        }
                    } else {
                        // Release jump but have a minimum height
                        if (jumpStats.isJumping && vel.vy > 0) {
                            vel.vy *= 0.5; // Cut jump height
                        }
                        jumpStats.isJumping = false;
                    }
                }
            }
        }
    }
}
