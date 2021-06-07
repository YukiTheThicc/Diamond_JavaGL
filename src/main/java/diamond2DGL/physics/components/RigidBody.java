package diamond2DGL.physics.components;

import diamond2DGL.Container;
import diamond2DGL.engComponents.Component;
import diamond2DGL.physics.enums.BodyType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class RigidBody extends Component {

    // ATTRIBUTES (necessary for Box2D)
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0;
    private float friction = 0.1f;
    private float angularVelocity = 0.0f;
    private float gravityScale = 1f;
    private BodyType bodyType = BodyType.Dynamic;
    private boolean fixedRotation = false;
    private boolean continuousCollision = true;
    private boolean isSensor;
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

    public float getFriction() {
        return friction;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public float getGravityScale() {
        return gravityScale;
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

    public boolean isSensor() {
        return isSensor;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity.set(velocity);
        if (rawBody != null) {
            this.rawBody.setLinearVelocity(new Vec2(velocity.x, velocity.y));
        }
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

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (rawBody != null) {
            this.rawBody.setAngularVelocity(angularVelocity);
        }
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (rawBody != null) {
            this.rawBody.setGravityScale(gravityScale);
        }
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

    public void setSensor(boolean sensor) {
        isSensor = sensor;
        if (rawBody != null) {
            if (sensor) {
                Container.getEnv().getPhysics().setIsSensor(this);
            } else {
                Container.getEnv().getPhysics().setNotSensor(this);
            }
        }
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    // METHODS
    public void addForce(Vector2f force) {
        if (rawBody != null) {
            rawBody.applyForceToCenter(new Vec2(velocity.x, velocity.y));
        }
    }

    public void addImpulse(Vector2f impulse) {
        if (rawBody != null) {
            rawBody.applyLinearImpulse(new Vec2(velocity.x, velocity.y), rawBody.getWorldCenter());
        }
    }

    @Override
    public void update(float dt) {
        if (rawBody != null) {
            this.parent.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
        }
        this.parent.transform.rotation = (float)Math.toDegrees(rawBody.getAngle());
    }
}
