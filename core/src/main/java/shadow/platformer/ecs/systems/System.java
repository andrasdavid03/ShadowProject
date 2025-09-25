package shadow.platformer.ecs.systems;

import java.util.List;

import shadow.platformer.ecs.entities.Entity;

public interface System {
    void update(float delta, List<Entity> entities);
}
