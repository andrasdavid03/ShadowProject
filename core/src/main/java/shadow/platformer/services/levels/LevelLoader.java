package shadow.platformer.services.levels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.Tile;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.services.tiles.TileType;

public class LevelLoader {
    private final TileRegistry tileRegistry;

    public LevelLoader(TileRegistry registry) {
        this.tileRegistry = registry;
    }

    public Entity loadLevel(String filename) {
        TiledMap map = new TmxMapLoader().load(filename);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("level1");
        
        int width = layer.getWidth();
        int height = layer.getHeight();

        int tileSize = layer.getTileWidth(); // Because its same x same 
        // Create tile matrix from layout
        Tile[][] tiles = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                int tileId = 0;
                if (cell != null && cell.getTile() != null) {
                    tileId = cell.getTile().getId(); // link to TileRegistry
                    TextureRegion region = cell.getTile().getTextureRegion();
                    TileType type = new TileType(region, true);

                    if (!tileRegistry.contains(tileId)) {
                        tileRegistry.register(tileId, type);
                    }
                }

                TileType type = tileRegistry.get(tileId);

                // Bounds for rendering/positioning
                Rectangle bounds = null;
                if (tileId != 0)
                    bounds = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
                else
                    bounds = null;

                tiles[y][x] = new Tile(tileId, type, bounds);
            }
        }

        TilemapComponent tilemap = new TilemapComponent(width, height, tileSize, tiles);

        Entity level = new Entity();
        level.addComponent(tilemap);

        return level;
    }
}
