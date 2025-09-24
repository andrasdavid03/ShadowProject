package shadow.platformer.ecs.components;

public class GravityComponent {
    public float gravity; // gravitational acceleration
    public float terminalVelocity; // max falling speed
    public boolean applyGravity = true;    // allow disabling temporarily

    public GravityComponent(float gravity, float terminalVelocity) {
        this.gravity = gravity;
        this.terminalVelocity = terminalVelocity;
    }
}