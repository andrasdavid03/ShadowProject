package shadow.platformer.ecs.components;

public class TransformComponent {
    public float x, y;
    public float width, height;
    public float rotation;

    public TransformComponent(float x, float y, float width, float height, float rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
