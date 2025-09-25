package shadow.platformer.services.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;

import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.services.tiles.Tile;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.services.tiles.TileType;

public class LevelLoader {
    private final TileRegistry tileRegistry;

    public LevelLoader(TileRegistry tileRegistry) {
        this.tileRegistry = tileRegistry;
    }

    public Entity loadLevel(String filename, String levelName) {
        // Load TMX map
        TiledMap map = new TmxMapLoader().load(filename);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(levelName);
        int width = layer.getWidth();
        int height = layer.getHeight();
        int tileSize = (int) layer.getTileWidth(); // assume square tiles

        Tile[][] tiles = new Tile[height][width];

        // Iterate over all cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                // Default = empty tile
                int tileId = 0;
                TileType type = null;
                Rectangle bounds = null;

                if (cell != null && cell.getTile() != null) {
                    TiledMapTile mapTile = cell.getTile();
                    tileId = mapTile.getId();

                    TextureRegion region = mapTile.getTextureRegion();
                    region.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

                    if (!tileRegistry.contains(tileId)) {
                        MapProperties props = mapTile.getProperties();

                        // Read "solid" property, default = false
                        boolean solid = props.containsKey("solid") && Boolean.parseBoolean(props.get("solid", String.class));

                        // Talan jo lesz
                        // int damage = props.get("damage", 0, Integer.class);
                        // String material = props.get("material", "default", String.class);

                        type = new TileType(region, solid);
                        tileRegistry.register(tileId, type);
                    } else {
                        type = tileRegistry.get(tileId);
                    }

                    // Bounds for positioning/collision
                    bounds = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
                }

                // Store tile in matrix
                tiles[y][x] = new Tile(tileId, type, bounds);
            }
        }

        // Build ECS entity
        TilemapComponent tilemap = new TilemapComponent(width, height, tileSize, tiles);

        Entity level = new Entity();
        level.addComponent(tilemap);
        return level;
    }
}
