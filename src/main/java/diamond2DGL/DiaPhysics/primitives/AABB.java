package diamond2DGL.DiaPhysics.primitives;

import diamond2DGL.DiaPhysics.rigidbody.RigidBody;
import org.joml.Vector2f;

public class AABB {

    // ATTRIBUTES
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody rigidBody = null;

    // CONSTRUCTORS
    public AABB() {
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    public AABB(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    // GETTERS & SETTERS
    public Vector2f getMin() {
        return new Vector2f(this.rigidBody.getPos()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidBody.getPos()).add(this.halfSize);
    }
}
