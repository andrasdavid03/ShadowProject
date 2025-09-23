package shadow.platformer.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import shadow.platformer.GameManager;
import shadow.platformer.ecs.components.*;
import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.*;
import shadow.platformer.ecs.systems.System;
import shadow.platformer.events.*;
import shadow.platformer.events.eventTypes.SpacePressedEvent;
import shadow.platformer.services.sound.LibGdxSoundService;
import shadow.platformer.services.sound.SoundService;
import shadow.platformer.services.tiles.TileType;
import shadow.platformer.factories.PlayerFactory;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class GameScreen implements Screen {
    private final GameManager game;
    private final List<Entity> entities = new ArrayList<>();
    private final List<shadow.platformer.ecs.systems.System> systems = new ArrayList<>();
    private final EventBus bus = new EventBus();
    private final SoundService soundService = new LibGdxSoundService();

    private Entity player;

    public GameScreen(GameManager game) {
        this.game = game;
        setupWorld();
    }

    private void setupWorld() {
        // Create player
        player = PlayerFactory.createPlayer(50, 50);
        entities.add(player);

        // Create tilemap
        Map<Integer, TileType> tileTypes = new HashMap<>();
        tileTypes.put(1, new TileType(new TextureRegion(new Texture("sprites/cat.jpg")), true));
        
        // Simple level entity
        Entity level = new Entity();
        int[][] tiles = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        level.addComponent(new TilemapComponent(tiles[0].length, tiles.length, 32, tiles));
        entities.add(level);

        // Game logic systems
        systems.add(new InputSystem(bus));
        systems.add(new TileCollisionSystem(tileTypes));
        systems.add(new MovementSystem());

        // Rendering systems
        systems.add(new TileRenderSystem(game.batch, tileTypes));
        systems.add(new RenderSystem(game.batch));

        // Event-driven sound
        bus.subscribe(SpacePressedEvent.class, e -> soundService.play("notification"));
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

