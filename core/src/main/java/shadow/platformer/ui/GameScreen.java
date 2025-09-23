package shadow.platformer.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import shadow.platformer.GameManager;

import shadow.platformer.entities.Entity;
import shadow.platformer.components.*;
import shadow.platformer.systems.*;
import shadow.platformer.systems.System;
import shadow.platformer.services.sound.LibGdxSoundService;
import shadow.platformer.services.sound.SoundService;
import shadow.platformer.event.*;
import shadow.platformer.event.eventTypes.SpacePressedEvent;
import shadow.platformer.factories.PlayerFactory;

import com.badlogic.gdx.graphics.GL20;


import java.util.*;

public class GameScreen implements Screen {
    private final GameManager game;
    private final List<Entity> entities = new ArrayList<>();
    private final List<shadow.platformer.systems.System> systems = new ArrayList<>();
    private final EventBus bus = new EventBus();
    private final SoundService soundService = new LibGdxSoundService();

    private Entity player;

    public GameScreen(GameManager game) {
        this.game = game;
        setupWorld();
    }

    private void setupWorld() {
        // Create player
        player = PlayerFactory.createPlayer(100, 100);
        entities.add(player);

        // Add systems
        systems.add(new InputSystem(bus));
        systems.add(new MovementSystem());
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

