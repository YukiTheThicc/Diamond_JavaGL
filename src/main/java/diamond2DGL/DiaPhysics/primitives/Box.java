package diamond2DGL.DiaPhysics.primitives;

import diamond2DGL.DiaPhysics.rigidbody.RigidBody;
import org.joml.Vector2f;

public class Box {

    // ATTRIBUTES
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody rigidBody = null;

    // CONSTRUCTORS
    public Box() {
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    public Box(Vector2f min, Vector2f max) {
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

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    // METHODS
    public Vector2f[] getVertices() {
        Vector2f min = getMin();
        Vector2f max = getMax();

        Vector2f[] vertices = {
            new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
            new Vector2f(max.x, min.y), new Vector2f(max.x, max.y)
        };
        if (rigidBody.getRotation() != 0.0f) {
            for (Vector2f vert : vertices) {
                //DiaMath.rotate(vert, this.rigidBody.getPos(), this.rigidBody.getRotation());
            }
        }

        return vertices;
    }
}
