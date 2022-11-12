package diamond2DGL.diaPhysics.rigidbody;

import diamond2DGL.engComponents.Component;
import org.joml.Vector2f;

public class RigidBody extends Component {

    // ATTRIBUTES
    private Vector2f pos = new Vector2f();
    private float rotation = 0.0f;

    // CONSTRUCTORS
    public RigidBody() {

    }

    // GETTERS & SETTERS
    public Vector2f getPos() {
        return pos;
    }

    public float getRotation() {
        return rotation;
    }
}
