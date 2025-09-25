package shadow.platformer.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import shadow.platformer.GameManager;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.*;
import shadow.platformer.ecs.systems.System;
import shadow.platformer.events.*;
import shadow.platformer.events.eventTypes.SpacePressedEvent;
import shadow.platformer.services.levels.LevelLoader;
import shadow.platformer.services.sound.LibGdxSoundService;
import shadow.platformer.services.sound.SoundService;
import shadow.platformer.services.tiles.TileRegistry;
import shadow.platformer.services.tiles.TileType;
import shadow.platformer.factories.PlayerFactory;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class GameScreen implements Screen {
    private final GameManager game;
    private final List<Entity> entities = new ArrayList<>();
    private final List<System> systems = new ArrayList<>();
    private final EventBus bus = new EventBus();
    private final SoundService soundService = new LibGdxSoundService();

    public GameScreen(GameManager game) {
        this.game = game;
        setupWorld();
    }

    private void setupWorld() {
        // Create player
        TextureRegion playerTexture = new TextureRegion(new Texture("sprites/cat.jpg"));
        PlayerFactory playerFactory = new PlayerFactory(playerTexture);

        Entity player = playerFactory.createPlayer(50, 50);
        entities.add(player);

        // Load level
        TileRegistry tileRegistry = new TileRegistry();

        //Texture dirtTexture = new Texture("sprites/dirt.png");
        //dirtTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        
        //tileRegistry.register(1, new TileType(new TextureRegion(dirtTexture), true));

        LevelLoader levelLoader = new LevelLoader(tileRegistry);
        entities.add(levelLoader.loadLevel("levels/level1.tmx"));

        // Game logic systems
        systems.add(new InputSystem(bus));
        systems.add(new GravitySystem());
        systems.add(new MovementSystem());

        // Rendering systems
        systems.add(new TileRenderSystem(game.batch, tileRegistry));
        systems.add(new RenderSystem(game.batch));

        // Event-driven sound
        bus.subscribe(SpacePressedEvent.class, e -> soundService.play("notification"));
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        // ECS update
        for (System sys : systems) {
            sys.update(delta, entities);
        }

        // Input handling
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

