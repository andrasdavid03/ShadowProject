package shadow.platformer.systems;

import shadow.platformer.entities.Entity;
import java.util.List;

public interface System {
    void update(float deltaTime, List<Entity> entities);
}
