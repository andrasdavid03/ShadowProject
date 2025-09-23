package shadow.platformer.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.SpriteComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;

public class PlayerFactory {
    public static Entity createPlayer(float x, float y) {
        Entity player = new Entity();
        player.addComponent(new TransformComponent(x, y, 32, 32, 0));
        player.addComponent(new VelocityComponent(0, 0));
        player.addComponent(new PlayerControllable());
        
        Texture tex = new Texture("sprites/cat.jpg");
        player.addComponent(new SpriteComponent(new TextureRegion(tex)));

        return player;
    }
}
