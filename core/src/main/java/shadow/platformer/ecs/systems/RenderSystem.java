package shadow.platformer.ecs.systems;

import shadow.platformer.ecs.components.SpriteComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.entities.Entity;

import java.util.List;
import com.badlogic.gdx.graphics.g2d.Batch;

public class RenderSystem implements System {
    private final Batch batch;

    public RenderSystem(Batch batch) {
        this.batch = batch;
    }

    @Override
    public void update(float delta, List<Entity> entities) {
        batch.begin();

        for (Entity e : entities) {
            if (e.hasComponent(TransformComponent.class) && e.hasComponent(SpriteComponent.class)) {
                SpriteComponent sprite = e.getComponent(SpriteComponent.class);
                TransformComponent tx = e.getComponent(TransformComponent.class);

                batch.draw(sprite.texture, tx.x, tx.y, tx.width, tx.height);
            }
        }

        batch.end();
    }
}
