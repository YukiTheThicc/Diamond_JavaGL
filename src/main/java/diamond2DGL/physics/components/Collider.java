package diamond2DGL.physics.components;

import diamond2DGL.engComponents.Component;
import org.joml.Vector2f;

public abstract class Collider extends Component {

    // ATTRIBUTES
    protected Vector2f offset = new Vector2f();

    // CONSTRUCTORS

    // GETTERS & SETTERS
    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }

    // METHODS
}
