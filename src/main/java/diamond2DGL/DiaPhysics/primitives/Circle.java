package diamond2DGL.DiaPhysics.primitives;

import diamond2DGL.DiaPhysics.rigidbody.RigidBody;
import org.joml.Vector2f;

public class Circle {

    // ATTRIBUTES
    private float radius = 1.0f;
    private RigidBody body = null;

    // CONSTRUCTORS
    public Circle() {

    }

    // GETTERS & SETTERS
    public float getRadius() {
        return radius;
    }

    public Vector2f getCenter() {
        return body.getPos();
    }
}
