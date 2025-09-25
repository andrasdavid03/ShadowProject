package shadow.platformer.ecs.components;

public class JumpStatsComponent {
    public float jumpForce; // initial jump velocity
    public int maxJumps;   // number of jumps before needing to touch ground
    public int jumpsLeft;  // jumps remaining
    public boolean isJumping = false; // whether the jump button is currently held

    public JumpStatsComponent(float jumpForce, int maxJumps) {
        this.jumpForce = jumpForce;
        this.maxJumps = maxJumps;
        this.jumpsLeft = maxJumps;
    }
}
