package shadow.platformer.ecs.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.events.EventBus;
import shadow.platformer.events.eventTypes.SpacePressedEvent;

public class InputSystem implements System {
    private static final float SPEED = 200f;

    private final EventBus bus;

    public InputSystem(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity e : entities) {
            if (e.hasComponent(VelocityComponent.class) && e.hasComponent(PlayerControllable.class)) {
                VelocityComponent vel = e.getComponent(VelocityComponent.class);
                vel.vx = 0;
                vel.vy = 0;
                if (Gdx.input.isKeyPressed(Input.Keys.W)) vel.vy = SPEED;
                if (Gdx.input.isKeyPressed(Input.Keys.S)) vel.vy = -SPEED;
                if (Gdx.input.isKeyPressed(Input.Keys.A)) vel.vx = -SPEED;
                if (Gdx.input.isKeyPressed(Input.Keys.D)) vel.vx = SPEED;
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && e.hasComponent(TransformComponent.class)) {
                    bus.publish(new SpacePressedEvent());
                }
            }
        }
    }
}
