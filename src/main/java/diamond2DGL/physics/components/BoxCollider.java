package diamond2DGL.physics.components;

import diamond2DGL.engComponents.Component;
import diamond2DGL.renderer.DebugDraw;
import org.joml.Vector2f;

public class BoxCollider extends Component {

    // ATTRIBUTES
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();
    private Vector2f offset = new Vector2f();

    // CONSTRUCTORS

    //GETTERS & SETTERS
    public Vector2f getHalfSize() {
        return halfSize;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }

    // METHODS
    @Override
    public void editorUpdate(float dT) {
        Vector2f center = new Vector2f(this.parent.transform.position).add(this.offset);
        DebugDraw.addBox(center, this.halfSize, this.parent.transform.rotation);
    }
}
