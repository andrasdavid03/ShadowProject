package shadow.platformer.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import shadow.platformer.GameManager;
import shadow.platformer.ecs.components.TilemapComponent;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.System;
import shadow.platformer.ecs.systems.control.InputSystem;
import shadow.platformer.ecs.systems.physics.GravitySystem;
import shadow.platformer.ecs.systems.physics.MovementSystem;
import shadow.platformer.ecs.systems.render.CameraFollowSystem;
import shadow.platformer.ecs.systems.render.RenderSystem;
import shadow.platformer.ecs.systems.render.TileRenderSystem;
import shadow.platformer.events.*;
import shadow.platformer.events.eventTypes.SpacePressedEvent;
import shadow.platformer.services.levels.LevelLoader;
import shadow.platformer.services.sound.LibGdxSoundService;
import shadow.platformer.services.sound.SoundService;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.factories.PlayerFactory;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class GameScreen implements Screen {
    private final GameManager game;
    private final List<Entity> entities = new ArrayList<>();
    private final List<System> logicSystems = new ArrayList<>();
    private final List<System> renderSystems = new ArrayList<>();
    private final SoundService soundService = new LibGdxSoundService();
    private final EventBus bus = new EventBus();

    public GameScreen(GameManager game) {
        this.game = game;
        setupWorld();
    }

    private void setupWorld() {
        // Create player
        TextureRegion playerTexture = new TextureRegion(new Texture("sprites/cat.jpg"));
        PlayerFactory playerFactory = new PlayerFactory(playerTexture);

        entities.add(playerFactory.createPlayer(50, 50));

        // Load level
        TileRegistry tileRegistry = new TileRegistry();

        LevelLoader levelLoader = new LevelLoader(tileRegistry);
        Entity level = levelLoader.loadLevel("levels/level1.tmx", "level1");
        entities.add(level);


        // Game logic systems
        logicSystems.add(new InputSystem(bus));
        logicSystems.add(new GravitySystem());
        logicSystems.add(new MovementSystem(level.getComponent(TilemapComponent.class)));

        
        // Rendering systems
        TilemapComponent tilemap = level.getComponent(TilemapComponent.class);
        int expectedTiles = tilemap.width * tilemap.height;
        TileRenderSystem tileRenderSystem = new TileRenderSystem(tileRegistry, expectedTiles, game.camera);
        tileRenderSystem.buildCache(tilemap);

        float worldWidth  = tilemap.width  * tilemap.tileSize;
        float worldHeight = 2000;

        game.cameraController.setWorldBounds(worldWidth, worldHeight);
        
        renderSystems.add(new CameraFollowSystem(game.cameraController));
        renderSystems.add(tileRenderSystem);
        renderSystems.add(new RenderSystem(game.batch));

        // Event-driven sound
        bus.subscribe(SpacePressedEvent.class, e -> soundService.play("notification"));
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ECS update
        for (System sys : logicSystems) {
            sys.update(delta, entities);
        }

        // Render update
        game.batch.setProjectionMatrix(game.camera.combined);
        for (System sys : renderSystems) {
            sys.update(delta, entities);
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

