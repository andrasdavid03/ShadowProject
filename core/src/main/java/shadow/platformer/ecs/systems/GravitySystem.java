package shadow.platformer.ecs.systems;

import java.util.List;

import shadow.platformer.ecs.components.GravityComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;

public class GravitySystem implements System{
    @Override
    public void update(float delta, List<Entity> entities) {
        for (Entity e : entities) {
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            GravityComponent grav = e.getComponent(GravityComponent.class);

            if (vel != null && grav != null) {
                vel.vy -= grav.gravity * delta;
                
                // Clamp to terminal velocity
                if (vel.vy < -grav.terminalVelocity) vel.vy = -grav.terminalVelocity;
            }
        }
    }
}
