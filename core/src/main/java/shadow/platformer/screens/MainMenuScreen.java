package shadow.platformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import shadow.platformer.GameManager;

public class MainMenuScreen implements Screen{
    private final GameManager game;

    public MainMenuScreen(GameManager game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        // BitmapFont or Scene2D (Reminder to self)
        // Example text
        String title = "Shadow Platformer";
        String prompt = "Press ENTER to Start";

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);
        font.draw(game.batch, title, Gdx.graphics.getWidth()/2f - 100, Gdx.graphics.getHeight()/2f + 50);
        font.getData().setScale(1);
        font.draw(game.batch, prompt, Gdx.graphics.getWidth()/2f - 100, Gdx.graphics.getHeight()/2f);

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game)); // switch to gameplay
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}