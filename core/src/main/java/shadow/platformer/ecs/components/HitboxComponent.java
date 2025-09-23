package shadow.platformer.ecs.components;

import com.badlogic.gdx.math.Rectangle;

public class HitboxComponent {
    public final Rectangle hitbox;

    public HitboxComponent(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
