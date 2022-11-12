package diamond2DGL.diaPhysics.primitives;

import org.joml.Vector2f;

public class Ray {

    // ATTRIBUTES
    private Vector2f origin;
    private Vector2f direction;

    // CONSTRUCTOR
    public Ray(Vector2f origin, Vector2f direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
    }

    // GETTERS & SETTERS
    public Vector2f getOrigin() {
        return origin;
    }

    public Vector2f getDirection() {
        return direction;
    }
}
