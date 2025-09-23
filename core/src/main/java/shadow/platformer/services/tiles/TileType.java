package shadow.platformer.services.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileType {
    public TextureRegion texture;
    public boolean solid;

    public TileType(TextureRegion texture, boolean solid) {
        this.texture = texture;
        this.solid = solid;
    }
}
