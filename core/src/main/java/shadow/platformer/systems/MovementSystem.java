package shadow.platformer.systems;

import java.util.List;

import shadow.platformer.components.TransformComponent;
import shadow.platformer.components.VelocityComponent;
import shadow.platformer.entities.Entity;

public class MovementSystem implements System {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity e : entities) {
            if (e.hasComponent(TransformComponent.class) && e.hasComponent(VelocityComponent.class)) {
                TransformComponent pos = e.getComponent(TransformComponent.class);
                VelocityComponent vel = e.getComponent(VelocityComponent.class);

                pos.x += vel.vx * deltaTime;
                pos.y += vel.vy * deltaTime;
            }
        }
    }
}
