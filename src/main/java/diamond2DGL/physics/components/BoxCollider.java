package diamond2DGL.physics.components;

import diamond2DGL.renderer.DebugDraw;
import org.joml.Vector2f;

public class BoxCollider extends Collider {

    // ATTRIBUTES
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    // CONSTRUCTORS

    //GETTERS & SETTERS
    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    // METHODS
    @Override
    public void editorUpdate(float dT) {
        Vector2f center = new Vector2f(this.parent.transform.position).add(this.offset);
        DebugDraw.addBox(center, this.halfSize, this.parent.transform.rotation);
    }
}
