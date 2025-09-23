package shadow.platformer.services.tiles;

import com.badlogic.gdx.math.Rectangle;

public class Tile {
    public int id;
    public TileType type;
    public Rectangle bounds;

    public Tile(int id, TileType type, Rectangle bounds) {
        this.id = id;
        this.type = type;
        this.bounds = bounds;
    }
}
