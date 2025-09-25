package shadow.platformer.ecs.systems;

import java.util.List;

import shadow.platformer.ecs.components.CameraFollowComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.camera.CameraController;

public class CameraFollowSystem implements System {
    private final CameraController cameraController;

    public CameraFollowSystem(CameraController camController) {
        this.cameraController = camController;
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        for (Entity e : entities) {
            if (e.hasComponent(CameraFollowComponent.class) && e.hasComponent(TransformComponent.class)) {
                TransformComponent t = e.getComponent(TransformComponent.class);
                cameraController.update(delta, t.x, t.y);
            }
        }
    }
}
