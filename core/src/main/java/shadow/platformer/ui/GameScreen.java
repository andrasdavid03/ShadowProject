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

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


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
        player = new Entity();
        player.addComponent(new TransformComponent(100, 100, 64, 64, 0));
        player.addComponent(new VelocityComponent(0, 0));
        Texture tex = new Texture("sprites/cat.jpg");
        player.addComponent(new SpriteComponent(new TextureRegion(tex)));
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

