package shadow.platformer.ecs.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent {
    public TextureRegion texture;

    public SpriteComponent(TextureRegion texture) {
        this.texture = texture;
    }
}
