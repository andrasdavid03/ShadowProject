package shadow.platformer.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import shadow.platformer.components.PlayerControllable;
import shadow.platformer.components.TransformComponent;
import shadow.platformer.components.VelocityComponent;
import shadow.platformer.components.SpriteComponent;
import shadow.platformer.entities.Entity;

public class PlayerFactory {
    public static Entity createPlayer(float x, float y) {
        Entity player = new Entity();
        player.addComponent(new TransformComponent(x, y, 64, 64, 0));
        player.addComponent(new VelocityComponent(0, 0));
        player.addComponent(new PlayerControllable());
        
        Texture tex = new Texture("sprites/cat.jpg");
        player.addComponent(new SpriteComponent(new TextureRegion(tex)));

        return player;
    }
}
