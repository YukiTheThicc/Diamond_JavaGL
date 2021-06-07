package diamond2DGL.physics.components;

import diamond2DGL.engComponents.Component;
import diamond2DGL.renderer.DebugDraw;
import org.joml.Vector2f;

public class CircleCollider extends Component {

    // ATTRIBUTES
    private float radius = 1f;
    private Vector2f offset = new Vector2f();

    // CONSTRUCTORS

    //GETTERS & SETTERS
    public float getRadius() {
        return radius;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }

    // METHODS

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.parent.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);
    }
}
