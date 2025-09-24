package shadow.platformer.ecs.components;

public class MovementStatsComponent {
    public float maxSpeed;
    public float acceleration;
    public float deceleration;

    public MovementStatsComponent(float maxSpeed, float acceleration, float deceleration) {
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
    }
}