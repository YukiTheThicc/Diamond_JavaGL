package diamond2DGL.physics;

import diamond2DGL.Entity;
import diamond2DGL.engComponents.Transform;
import diamond2DGL.physics.components.BoxCollider;
import diamond2DGL.physics.components.CircleCollider;
import diamond2DGL.physics.components.PillboxCollider;
import diamond2DGL.physics.components.RigidBody;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class Physics {

    // ATTRIBUTES
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);
    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    // CONSTRUCTORS
    public Physics() {
        world.setContactListener(new DiaContactListener());
    }

    // GETTERS & SETTERS
    public Vector2f getGravity() {
        return new Vector2f(this.world.getGravity().x, this.world.getGravity().y);
    }

    public World getWorld() {
        return world;
    }

    // METHODS
    public void add(Entity entity) {
        RigidBody rb = entity.getComponent(RigidBody.class);
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = entity.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();
            bodyDef.gravityScale = rb.getGravityScale();
            bodyDef.angularVelocity = rb.getAngularVelocity();
            bodyDef.userData = rb.parent;

            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);
            CircleCollider circleCollider;
            BoxCollider boxCollider;
            PillboxCollider pillboxCollider;

            if ((circleCollider = entity.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rb, circleCollider);
            }
            if ((boxCollider = entity.getComponent(BoxCollider.class)) != null) {
                addBoxCollider(rb, boxCollider);
            }
            if ((pillboxCollider = entity.getComponent(PillboxCollider.class)) != null) {
                addPillboxCollider(rb, pillboxCollider);
            }
        }
    }

    public void destroyEntity(Entity e){
        RigidBody rb = e.getComponent(RigidBody.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void setIsSensor(RigidBody rb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = true;
            fixture = fixture.m_next;
        }
    }

    public void setNotSensor(RigidBody rb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = false;
            fixture = fixture.m_next;
        }
    }

    public void resetBoxCollider(RigidBody rb, BoxCollider boxCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;
        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }
        addBoxCollider(rb, boxCollider);
        body.resetMassData();
    }

    public void resetCircleCollider(RigidBody rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;
        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }
        addCircleCollider(rb, circleCollider);
        body.resetMassData();
    }

    public void resetPillboxCollider(RigidBody rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        if (body == null) return;
        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }
        addPillboxCollider(rb, pb);
        body.resetMassData();
    }

    public void addBoxCollider(RigidBody rb, BoxCollider boxCollider) {
        Body body = rb.getRawBody();
        assert  body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = boxCollider.getOffset();
        Vector2f origin = new Vector2f(boxCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = boxCollider.parent;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addCircleCollider(RigidBody rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        assert  body != null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = circleCollider.parent;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addPillboxCollider(RigidBody rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        assert  body != null : "Raw body must not be null";

        addBoxCollider(rb, pb.getBox());
        addCircleCollider(rb, pb.getTopCircle());
        addCircleCollider(rb, pb.getBottomCircle());
    }

    public RayCastInfo rayCast(Entity entity, Vector2f point1, Vector2f point2) {
        RayCastInfo callback = new RayCastInfo(entity);
        world.raycast(callback, new Vec2(point1.x, point1.y), new Vec2(point2.x, point2.y));
        return callback;
    }

    public void reset() {
        physicsTime = 0;
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        return size;
    }

    public boolean isLocked() {
        return world.isLocked();
    }
}
