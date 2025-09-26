package shadow.platformer.ecs.components;

import shadow.platformer.services.tiles.Tile;

public class TilemapComponent {
    public int width, height; // in tiles
    public int tileSize; // in pixels
    public Tile[][] tiles;

    public TilemapComponent(int width, int height, int tileSize, Tile[][] tiles) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = tiles;
    }
}