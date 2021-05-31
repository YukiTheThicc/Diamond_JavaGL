package diamond2DGL.physics.components;

import diamond2DGL.engComponents.Component;
import diamond2DGL.physics.enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class RigidBody extends Component {

    // ATTRIBUTES (necessary for Box2D)
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;
    private boolean fixedRotation = false;
    private boolean continuousCollision = true;
    private transient Body rawBody = null;

    // CONSTRUCTORS

    // GETTERS & SETTERS
    public Vector2f getVelocity() {
        return velocity;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public float getMass() {
        return mass;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    // METHODS

    @Override
    public void update(float dT) {
        if (rawBody != null) {
            this.parent.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
        }
        this.parent.transform.rotation = (float)Math.toDegrees(rawBody.getAngle());
    }
}
