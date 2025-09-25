package shadow.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import shadow.platformer.screens.MainMenuScreen;
import shadow.platformer.services.camera.CameraController;

public class GameManager extends Game {
    public static final int VIRTUAL_WIDTH = 320;
    public static final int VIRTUAL_HEIGHT = 180;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public CameraController cameraController;
    public Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        camera.position.set(160, 90, 0); // center camera

        cameraController = new CameraController(camera);
        cameraController.setDeadZone(48, 32);
        cameraController.setFollowSpeed(5f);
        // world bounds will be set later, when level is loaded

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}