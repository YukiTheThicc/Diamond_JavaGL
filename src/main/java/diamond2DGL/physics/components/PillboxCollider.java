package diamond2DGL.physics.components;

import diamond2DGL.Container;
import diamond2DGL.engComponents.Component;
import org.joml.Vector2f;

public class PillboxCollider extends Component {

    // ATTRIBUTES
    private transient CircleCollider topCircle = new CircleCollider();
    private transient CircleCollider bottomCircle = new CircleCollider();
    private transient BoxCollider box = new BoxCollider();
    private transient boolean resetFixture = false;
    public float width = 0.125f;
    public float height = 0.25f;
    public Vector2f offset = new Vector2f();

    // CONSTRUCTORS

    // GETTERS & SETTERS
    public CircleCollider getTopCircle() {
        return topCircle;
    }

    public CircleCollider getBottomCircle() {
        return bottomCircle;
    }

    public BoxCollider getBox() {
        return box;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setTopCircle(CircleCollider topCircle) {
        this.topCircle = topCircle;
    }

    public void setBottomCircle(CircleCollider bottomCircle) {
        this.bottomCircle = bottomCircle;
    }

    public void setBox(BoxCollider box) {
        this.box = box;
    }

    public void setWidth(float width) {
        this.width = width;
        recalculateColliders();
        resetFixture();
    }

    public void setHeight(float height) {
        this.height = height;
        recalculateColliders();
        resetFixture();
    }

    // METHODS
    @Override
    public void start() {
        this.topCircle.parent = this.parent;
        this.bottomCircle.parent = this.parent;
        this.box.parent = this.parent;
        recalculateColliders();
    }

    public void resetFixture() {
        if (Container.getEnv().getPhysics().isLocked()) {
            resetFixture = true;
            return;
        }
        resetFixture = false;
        if (parent != null) {
            RigidBody rb = parent.getComponent(RigidBody.class);
            if (rb != null) {
                Container.getEnv().getPhysics().resetPillboxCollider(rb, this);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (resetFixture) {
            resetFixture();
        }
    }

    @Override
    public void editorUpdate(float dt) {
        topCircle.editorUpdate(dt);
        bottomCircle.editorUpdate(dt);
        box.editorUpdate(dt);

        if (resetFixture) {
            resetFixture();
        }
    }

    private void recalculateColliders() {
        float circleRadius = width / 4f;
        float boxHeight = height - 2 * circleRadius;
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(offset).add(0, boxHeight / 4f));
        bottomCircle.setOffset(new Vector2f(offset).sub(0, boxHeight / 4f));
        box.setHalfSize(new Vector2f(width / 2f, boxHeight / 2f));
        box.setOffset(offset);
    }
}
