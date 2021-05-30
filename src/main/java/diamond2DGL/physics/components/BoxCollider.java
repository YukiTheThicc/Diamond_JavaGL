package diamond2DGL.physics.components;

import diamond2DGL.engComponents.Component;
import org.joml.Vector2f;

public class BoxCollider extends Component {

    // ATTRIBUTES
    private Vector2f halfSize = new Vector2f(1);

    // CONSTRUCTORS

    //GETTERS & SETTERS
    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    // METHODS

}
