package shadow.platformer.factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import shadow.platformer.ecs.components.GravityComponent;
import shadow.platformer.ecs.components.HitboxComponent;
import shadow.platformer.ecs.components.JumpStatsComponent;
import shadow.platformer.ecs.components.MovementStatsComponent;
import shadow.platformer.ecs.components.PlayerControllable;
import shadow.platformer.ecs.components.SpriteComponent;
import shadow.platformer.ecs.components.TransformComponent;
import shadow.platformer.ecs.components.VelocityComponent;
import shadow.platformer.ecs.entities.Entity;

public class PlayerFactory {
    private final TextureRegion playerTexture;

    public PlayerFactory(TextureRegion playerTexture) {
        this.playerTexture = playerTexture;
    }

    public Entity createPlayer(float x, float y) {
        Entity player = new Entity();

        float width = 16;
        float height = 16;

        player.addComponent(new TransformComponent(x, y, width, height, 0));
        player.addComponent(new VelocityComponent(0, 0));
        player.addComponent(new MovementStatsComponent(200, 50, 50));
        player.addComponent(new JumpStatsComponent(350f, 2));
        player.addComponent(new GravityComponent(1200, 400));
        player.addComponent(new HitboxComponent(new Rectangle(x, y, width, height)));
        player.addComponent(new PlayerControllable());
        player.addComponent(new SpriteComponent(playerTexture));

        return player;
    }
}