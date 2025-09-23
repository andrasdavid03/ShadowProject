package shadow.platformer.ecs.components;

public class TilemapComponent {
    public int width, height;
    public int tileSize;
    public int[][] tiles;

    public TilemapComponent(int width, int height, int tileSize, int[][] tiles) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.tiles = tiles;
    }
}