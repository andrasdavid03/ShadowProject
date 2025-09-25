package shadow.platformer.services.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class CameraController {
    private final OrthographicCamera camera;
    private final Vector3 target = new Vector3();

    // Dead zone size (the box around the player where the camera won’t move)
    private float deadZoneWidth = 48;
    private float deadZoneHeight = 32;

    // Smoothness (higher = snappier, lower = floatier)
    private float followSpeed = 5f;

    // World bounds (optional, set later)
    private float worldWidth = -1;
    private float worldHeight = -1;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setDeadZone(float width, float height) {
        this.deadZoneWidth = width;
        this.deadZoneHeight = height;
    }

    public void setFollowSpeed(float speed) {
        this.followSpeed = speed;
    }

    public void setWorldBounds(float width, float height) {
        this.worldWidth = width;
        this.worldHeight = height;
    }

    public void update(float delta, float playerX, float playerY) {
        float left = camera.position.x - deadZoneWidth / 2f;
        float right = camera.position.x + deadZoneWidth / 2f;
        float bottom = camera.position.y - deadZoneHeight / 2f;
        float top = camera.position.y + deadZoneHeight / 2f;

        float targetX = camera.position.x;
        float targetY = camera.position.y;

        // Move target position if player leaves dead zone
        if (playerX < left) targetX = playerX + deadZoneWidth / 2f;
        if (playerX > right) targetX = playerX - deadZoneWidth / 2f;
        if (playerY < bottom) targetY = playerY + deadZoneHeight / 2f;
        if (playerY > top) targetY = playerY - deadZoneHeight / 2f;

        target.set(targetX, targetY, 0);

        // Smooth follow (lerp)
        camera.position.lerp(target, followSpeed * delta);

        // Clamp to world bounds (if set)
        if (worldWidth > 0 && worldHeight > 0) {
            float halfW = camera.viewportWidth / 2f;
            float halfH = camera.viewportHeight / 2f;

            camera.position.x = MathUtils.clamp(camera.position.x, halfW, worldWidth - halfW);
            camera.position.y = MathUtils.clamp(camera.position.y, halfH, worldHeight - halfH);
        }

        camera.update();
    }
}