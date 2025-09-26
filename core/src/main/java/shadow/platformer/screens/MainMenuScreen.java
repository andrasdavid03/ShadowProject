package shadow.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import shadow.platformer.GameManager;

public class MainMenuScreen implements Screen{
    private final GameManager game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private String[] options = {"Start", "Exit"};
    private int selectedIndex = 0;

    public MainMenuScreen(GameManager game) {
        this.game = game;
        this.batch = game.batch;

        font = new BitmapFont();
        font.getData();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        batch.begin();

        GlyphLayout layout = new GlyphLayout();
        float spacing = font.getLineHeight() + 4; // 4 pixels padding
        float totalHeight = options.length * spacing;

        for (int i = 0; i < options.length; i++) {
            String text = options[i];
            layout.setText(font, text);

            // Center horizontally
            float x = (game.viewport.getWorldWidth() - layout.width) / 2;

            // Center vertically
            float y = (game.viewport.getWorldHeight() + totalHeight) / 2 - i * spacing;

            font.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            font.draw(batch, text, x, y);
        }

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedIndex = (selectedIndex + 1) % options.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (options[selectedIndex].equals("Start")) {
                // TODO Add level selection later or at least load the correct level
                game.setScreen(new GameScreen(game, "level1"));
            } else if (options[selectedIndex].equals("Exit")) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int w, int h) {
        // May be needed later
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        font.dispose();
    }
}