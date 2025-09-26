package shadow.platformer.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import shadow.platformer.GameManager;
import shadow.platformer.ecs.World;
import shadow.platformer.ecs.WorldStore;
import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.control.InputSystem;
import shadow.platformer.ecs.systems.physics.GravitySystem;
import shadow.platformer.ecs.systems.physics.MovementSystem;
import shadow.platformer.ecs.systems.render.CameraFollowSystem;
import shadow.platformer.ecs.systems.render.RenderSystem;
import shadow.platformer.ecs.systems.render.TileRenderSystem;
import shadow.platformer.events.*;
import shadow.platformer.events.eventTypes.SpacePressedEvent;
import shadow.platformer.services.ServiceLocator;
import shadow.platformer.services.levels.LevelLoader;
import shadow.platformer.services.sound.LibGdxSoundService;
import shadow.platformer.services.sound.SoundService;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.factories.PlayerFactory;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen {
    private final GameManager game;
    private final String levelId;
    
    private World world;

    public GameScreen(GameManager game, String levelId) {
        this.game = game;
        this.levelId = levelId;

        ServiceLocator.register(SoundService.class, new LibGdxSoundService());

        this.world = WorldStore.getOrCreate(levelId, this::buildWorld);
    }

    // Build a fresh world (only called the first time for a level)
    private World buildWorld() {
        World w = new World();
        EventBus bus = w.bus;

        // Services
        TileRegistry tileRegistry = new TileRegistry();
        ServiceLocator.register(TileRegistry.class, tileRegistry);

        LevelLoader levelLoader = new LevelLoader(tileRegistry);
        ServiceLocator.register(LevelLoader.class, levelLoader);

        SoundService soundService = ServiceLocator.get(SoundService.class);

        // Entities
        TextureRegion playerTexture = new TextureRegion(new Texture("sprites/cat.jpg"));
        PlayerFactory playerFactory = new PlayerFactory(playerTexture);
        Entity player = playerFactory.createPlayer(50, 50);
        w.entities.add(player);

        // Level entity with tilemap
        // TODO: I have to make it so that I can load different levels here
        Entity level = levelLoader.loadLevel("levels/level1.tmx", levelId);
        w.entities.add(level);

        // Logic systems - priority controls order
        w.addLogicSystem(new InputSystem(bus)); // priority default 0
        w.addLogicSystem(new GravitySystem()); // later than input
        w.addLogicSystem(new MovementSystem(level.getComponent(TilemapComponent.class)));

        // Rendering systems
        var tilemap = level.getComponent(TilemapComponent.class);
        int expectedTiles = tilemap.width * tilemap.height;
        TileRenderSystem tileRenderSystem = new TileRenderSystem(tileRegistry, expectedTiles, game.camera);
        tileRenderSystem.buildCache(tilemap);

        float worldWidth  = tilemap.width  * tilemap.tileSize;
        float worldHeight = Math.max(tilemap.tileSize * tilemap.height, 2000f);
        game.cameraController.setWorldBounds(worldWidth, worldHeight);

        w.addRenderSystem(new CameraFollowSystem(game.cameraController));
        w.addRenderSystem(tileRenderSystem);
        w.addRenderSystem(new RenderSystem(game.batch));

        // Event-driven sound example
        bus.subscribe(SpacePressedEvent.class, e -> {
            if (soundService != null) soundService.play("notification");
        });

        return w;
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);

        // Render world (render systems run regardless of paused state so overlays can show)
        game.batch.setProjectionMatrix(game.camera.combined);
        world.render(delta);
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() { world.setPaused(true); }
    @Override public void resume() { world.setPaused(false); }
    @Override public void hide() {}
    @Override public void dispose() {}
}

